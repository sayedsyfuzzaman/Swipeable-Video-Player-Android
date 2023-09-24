package com.nexdecade.nd_shorts.data

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("images/sample_shorts.json")
    suspend fun getShorts(@Query("rnd") randomNumber: Int): ShortsAPIResponse
}