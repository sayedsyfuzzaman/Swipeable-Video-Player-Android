package com.syfuzzaman.swipeable_video_player_android.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("images/sample_shorts.json")
    suspend fun getShorts(@Query("rnd") randomNumber: Int): ShortsAPIResponse
}