package me.cniekirk.cryptotracker.data.remote

import me.cniekirk.cryptotracker.data.model.AssetData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * The CoinCap API Retrofit interface
 */
interface CoinCapApi {

    @GET("v2/assets")
    suspend fun getAssets(
        @Header("Authorization") authorization: String = "Bearer bf770a0b-0a3b-4556-b4c6-1c4de211a5d7",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<AssetData>
}