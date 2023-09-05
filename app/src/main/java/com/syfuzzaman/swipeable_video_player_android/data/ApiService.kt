package com.syfuzzaman.swipeable_video_player_android.data

import javax.inject.Inject

class ApiService @Inject constructor(
    private val mApi: Api
){
    suspend fun execute():ShortsAPIResponse{
        return  tryIO {
            mApi.getShorts()
        }
    }
}