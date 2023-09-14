package com.syfuzzaman.swipeable_video_player_android.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoPlaybackDuration(
    @PrimaryKey(autoGenerate = false)
    val contentId: Int ?= null,
    val lastPlaybackDuration: Long ?= null
)
