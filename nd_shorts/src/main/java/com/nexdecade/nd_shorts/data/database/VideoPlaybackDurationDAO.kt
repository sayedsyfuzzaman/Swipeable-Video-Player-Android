package com.nexdecade.nd_shorts.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao // DATA ACCESS OBJECT
interface VideoPlaybackDurationDAO {

    /*  Suspend function | Asynchronous programming
        Run in coroutine
        Block it until database operation finished  */
    @Upsert // Insert or Update
    suspend fun insertLastPlaybackDuration(videoPlaybackDuration: VideoPlaybackDuration)

    @Delete
    suspend fun removePlaybackDuration(videoPlaybackDuration: VideoPlaybackDuration)

    @Query("SELECT * FROM VideoPlaybackDuration WHERE contentId = :contentId ORDER BY contentId DESC LIMIT 1")
    suspend fun getPlaybackDurationByContentId(contentId: Int): VideoPlaybackDuration?
}