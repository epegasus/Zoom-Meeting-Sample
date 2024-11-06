package dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network

import dev.pegasus.zoommeetingsample.oneToOne.domain.entities.AccessTokenResponse
import dev.pegasus.zoommeetingsample.oneToOne.domain.entities.ZakResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

interface ApiInterface {

    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Call<AccessTokenResponse>

    @GET("users/{userId}/token?type=zak")
    fun getZAKToken(
        @Path("userId") userId: String,
        @Header("Authorization") authHeader: String
    ): Call<ZakResponse>
}