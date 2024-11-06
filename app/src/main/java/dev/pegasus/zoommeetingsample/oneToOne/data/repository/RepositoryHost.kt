package dev.pegasus.zoommeetingsample.oneToOne.data.repository

import android.util.Log
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.local.TokenManager
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network.ZoomManager
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.local.ZoomConfig
import dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network.RetrofitInstance
import dev.pegasus.zoommeetingsample.oneToOne.domain.entities.AccessTokenResponse
import dev.pegasus.zoommeetingsample.oneToOne.domain.entities.ZakResponse
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class RepositoryHost(private val zoomManager: ZoomManager) {

    fun initSdk(jwtToken: String, callback: (Boolean) -> Unit) {
        zoomManager.initSdk(jwtToken, callback)
    }

    fun generateMeetingJwtToken(): String {
        return TokenManager().generateMeetingSdkJwt()
    }

    fun getAuthorizationUrl(): String {
        return ZoomConfig().authorizationUrl
    }

    fun getCredentials(): Pair<String, String> {
        val zoomConfig = ZoomConfig()
        return Pair(zoomConfig.zoomClientId, zoomConfig.zoomClientSecret)
    }

    fun getAccessToken(basicAuth: String, grantType: String, authorizationCode: String, redirectUri: String, callback: (String?) -> Unit) {
        RetrofitInstance
            .accessTokenApi
            .getAccessToken(
                authorization = basicAuth,
                grantType = grantType,
                code = authorizationCode,
                redirectUri = redirectUri
            )
            .enqueue(object : Callback<AccessTokenResponse> {
                override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { accessTokenResponse: AccessTokenResponse ->
                            val accessToken = accessTokenResponse.accessToken
                            Log.d(TAG, "AccessTokenResponse: $accessTokenResponse")
                            Log.d(TAG, "AccessToken: $accessToken")
                            callback.invoke(accessToken)
                        }
                    } else {
                        Log.e(TAG, "Token exchange failed: " + response.message())
                        callback.invoke(null)
                    }
                }

                override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                    Log.e(TAG, "Network error: " + t.message)
                    callback.invoke(null)
                }
            })
    }

    fun getZakToken(userId: String, authHeader: String, callback: (String?) -> Unit) {
        RetrofitInstance
            .zakApi
            .getZAKToken(
                userId = userId,
                authHeader = authHeader
            )
            .enqueue(object :Callback<ZakResponse> {
                override fun onResponse(call: Call<ZakResponse>, response: Response<ZakResponse>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: ${response.body()}")
                        val zakToken = response.body()?.token
                        callback(zakToken)
                    } else {
                        Log.e(TAG, "Failed to get ZAK token. Error code: ${response.code()}")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ZakResponse>, t: Throwable) {
                    Log.e(TAG, "Error fetching ZAK token: ${t.message}")
                    callback(null)
                }
            })
    }


    fun startMeeting(zakToken: String, callback: (Int) -> Unit) {
        zoomManager.startMeeting(zakToken, callback)
    }
}