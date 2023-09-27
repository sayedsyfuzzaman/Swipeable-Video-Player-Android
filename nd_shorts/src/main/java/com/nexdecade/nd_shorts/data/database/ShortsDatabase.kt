package com.nexdecade.nd_shorts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        VideoPlaybackDuration::class
    ],
    version = 1
)
abstract class ShortsDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "nd-shorts-db"
    }
    abstract fun getVideoPlaybackDurationDAO(): VideoPlaybackDurationDAO
}