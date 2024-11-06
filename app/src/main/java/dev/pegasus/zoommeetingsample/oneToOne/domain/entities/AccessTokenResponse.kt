package dev.pegasus.zoommeetingsample.oneToOne.domain.entities

import com.google.gson.annotations.SerializedName

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

data class AccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("scope") val scope: String
)