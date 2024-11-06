package dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.local

import android.util.Log
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.CLIENT_ID
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.CLIENT_SECRET
import dev.pegasus.zoommeetingsample.utilities.utils.ConstantUtils.TAG

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/1/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

class TokenManager {

    fun generateMeetingSdkJwt(): String {
        // Set the issued at and expiration times (in seconds since epoch)
        val iat = System.currentTimeMillis() / 1000
        val exp = iat + 129600

        // Create the JWT token
        val algorithm = Algorithm.HMAC256(CLIENT_SECRET)
        val token = JWT.create()
            .withHeader(mapOf("alg" to "HS256", "typ" to "JWT"))
            .withClaim("appKey", CLIENT_ID)
            .withClaim("iat", iat)
            .withClaim("exp", exp)
            .withClaim("tokenExp", exp)
            .sign(algorithm)

        Log.d(TAG, "generateMeetingSdkJwt: jwtToken: $token")
        return token
    }
}