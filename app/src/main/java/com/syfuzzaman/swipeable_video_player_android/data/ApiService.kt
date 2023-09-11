package com.syfuzzaman.swipeable_video_player_android.data

import com.syfuzzaman.swipeable_video_player_android.utils.tryIO
import javax.inject.Inject
import kotlin.random.Random

class ApiService @Inject constructor(
    private val mApi: Api
){
    suspend fun execute():ShortsAPIResponse{
        return  tryIO {
            mApi.getShorts(Random(50000).nextInt())
        }
    }
}