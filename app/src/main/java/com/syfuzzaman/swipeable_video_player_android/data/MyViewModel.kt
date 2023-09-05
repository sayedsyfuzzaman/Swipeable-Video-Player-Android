package com.syfuzzaman.swipeable_video_player_android.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel(){
    val shortsResponse =  SingleLiveEvent<Resource<ShortsAPIResponse>>()

    fun getShortsResponse(){
        viewModelScope.launch {
            val response = resultFromResponse { apiService.execute() }
            shortsResponse.value = response
        }
    }
}