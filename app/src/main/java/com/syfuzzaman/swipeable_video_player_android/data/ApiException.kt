package com.syfuzzaman.swipeable_video_player_android.data

data class ApiException(val errorCode:Int, val errorMessage: String):Exception(errorMessage)