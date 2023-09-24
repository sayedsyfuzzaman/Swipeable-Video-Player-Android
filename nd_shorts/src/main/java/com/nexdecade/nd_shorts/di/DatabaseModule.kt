package com.nexdecade.nd_shorts.di

import android.content.Context
import androidx.room.Room
import com.nexdecade.nd_shorts.data.database.ShortsDatabase
import com.nexdecade.nd_shorts.data.database.VideoPlaybackDurationDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext app: Context) : ShortsDatabase {
        return Room.databaseBuilder(
            app,
            ShortsDatabase::class.java,
            ShortsDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesVideoPlaybackDurationDAO(db: ShortsDatabase) : VideoPlaybackDurationDAO {
        return db.getVideoPlaybackDurationDAO()
    }

}