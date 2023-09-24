package com.syfuzzaman.swipeable_video_player_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexdecade.nd_shorts.data.ShortsBean
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    val autoPlayShorts =  MutableLiveData<List<ShortsBean>>()
}