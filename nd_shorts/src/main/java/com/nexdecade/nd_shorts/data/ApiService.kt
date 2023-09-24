package com.nexdecade.nd_shorts.data

import com.nexdecade.nd_shorts.utils.tryIO
import javax.inject.Inject
import kotlin.random.Random

class ApiService @Inject constructor(
    private val mApi: Api
){
    suspend fun execute(): ShortsAPIResponse {
        return  tryIO {
            mApi.getShorts(Random(50000).nextInt())
        }
    }
}