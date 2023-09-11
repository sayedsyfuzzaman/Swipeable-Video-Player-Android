package com.syfuzzaman.swipeable_video_player_android.common

import androidx.media3.common.Player

interface PlayerStateCallback {
    fun onVideoDurationRetrieved(duration: Long, player: Player)
    fun onVideoBuffering(player: Player)
    fun onStartedPlaying(player: Player)
    fun onFinishedPlaying(player: Player)
}