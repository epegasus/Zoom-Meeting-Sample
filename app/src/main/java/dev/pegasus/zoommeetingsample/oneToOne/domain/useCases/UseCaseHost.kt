package dev.pegasus.zoommeetingsample.oneToOne.domain.useCases

import android.content.Intent
import android.net.Uri
import android.util.Base64
import dev.pegasus.zoommeetingsample.oneToOne.data.repository.RepositoryHost

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class UseCaseHost(private val repositoryHost: RepositoryHost) {

    fun initSdk(callback: (Boolean) -> Unit) {
        val jwtToken = repositoryHost.generateMeetingJwtToken()
        repositoryHost.initSdk(jwtToken, callback)
    }

    fun getAuthorizationCodeIntent(): Intent {
        val authorizationUrl = repositoryHost.getAuthorizationUrl()
        return Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl))
    }

    fun onNewIntent(intent: Intent, callback: (Int) -> Unit) {
        val data = intent.data
        if (data != null && "yourapp" == data.scheme) {
            val authorizationCode = data.getQueryParameter("code")
            if (authorizationCode != null) {
                getAccessTokenForMeeting(authorizationCode, callback)
                return
            }
        }
        callback.invoke(2)
    }

    private fun getAccessTokenForMeeting(authorizationCode: String, callback: (Int) -> Unit) {
        val credentials = repositoryHost.getCredentials()
        val basicAuth = "Basic " + Base64.encodeToString("${credentials.first}:${credentials.second}".toByteArray(), Base64.NO_WRAP)

        repositoryHost.getAccessToken(
            basicAuth = basicAuth,
            grantType = "authorization_code",
            authorizationCode = authorizationCode,
            redirectUri = "yourapp://callback"
        ) {
            if (it == null) {
                callback.invoke(2)
                return@getAccessToken
            }
            getZakTokenForMeeting(it, callback)
        }
    }

    private fun getZakTokenForMeeting(accessToken: String, callback: (Int) -> Unit) {
        val authHeader = "Bearer $accessToken"
        repositoryHost.getZakToken("me", authHeader) {
            if (it == null) {
                callback.invoke(2)
                return@getZakToken
            }
            startMeeting(it, callback)
        }
    }

    private fun startMeeting(zakToken: String, callback: (Int) -> Unit) {
        repositoryHost.startMeeting(zakToken, callback)
    }
}