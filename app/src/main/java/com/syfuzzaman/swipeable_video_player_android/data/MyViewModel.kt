package com.syfuzzaman.swipeable_video_player_android.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syfuzzaman.swipeable_video_player_android.utils.SingleLiveEvent
import com.syfuzzaman.swipeable_video_player_android.utils.resultFromResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel(){
    val shortsResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsAutoPlayResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsGridResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    val shortsBannerResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()
    var featuredJob: Job? = null
    var swipeJob = MutableLiveData<Boolean>()

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


}