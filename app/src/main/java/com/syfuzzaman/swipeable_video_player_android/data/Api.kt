package com.syfuzzaman.swipeable_video_player_android.data

import retrofit2.http.GET

interface Api {
    @GET("images/sample_shorts.json?rnd=24368219491642")
    suspend fun getShorts(): ShortsAPIResponse
}