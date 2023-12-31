package com.nexdecade.nd_shorts.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexdecade.nd_shorts.data.database.VideoPlaybackDuration
import com.nexdecade.nd_shorts.data.database.VideoPlaybackDurationDAO
import com.nexdecade.nd_shorts.utils.SingleLiveEvent
import com.nexdecade.nd_shorts.utils.resultFromResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val apiService: ApiService,
    private val videoPlaybackDurationDAO: VideoPlaybackDurationDAO
) : ViewModel(){
    val shortsResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsAutoPlayResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsGridResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsBannerResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    var featuredJob: Job? = null
    var swipeJob = MutableLiveData<Boolean>()
    var autoPlayFragmentFullyVisible = MutableLiveData<Boolean>()

    var contentWiseLastPlaybackDuration = MutableLiveData<VideoPlaybackDuration>()

    fun getShortsResponse(){
        viewModelScope.launch {
            val response = resultFromResponse { apiService.execute() }
            shortsResponse.value = response
        }
    }

    fun getShortsAutoPlayResponse(){
        viewModelScope.launch {
            val response = resultFromResponse { apiService.execute() }
            shortsAutoPlayResponse.value = response
        }
    }
    fun getShortsGridResponse(){
        viewModelScope.launch {
            val response = resultFromResponse { apiService.execute() }
            shortsGridResponse.value = response
        }
    }

    fun getShortsBannerResponse(){
        viewModelScope.launch {
            val response = resultFromResponse { apiService.execute() }
            shortsBannerResponse.value = response
        }
    }

    // Room DB
    fun upsertVideoPlaybackDuration(videoPlaybackDuration: VideoPlaybackDuration){
        viewModelScope.launch(Dispatchers.IO){
            videoPlaybackDurationDAO.insertLastPlaybackDuration(videoPlaybackDuration)
        }
    }

    suspend fun getLastVideoPlayback(contentId: Int): VideoPlaybackDuration? {
//        viewModelScope.launch(Dispatchers.IO){
            return videoPlaybackDurationDAO.getPlaybackDurationByContentId(contentId)
//            contentWiseLastPlaybackDuration.postValue(response)
//        }
    }


}