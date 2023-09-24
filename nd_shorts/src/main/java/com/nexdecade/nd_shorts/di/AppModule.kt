package com.nexdecade.nd_shorts.di

import android.content.Context
import android.content.SharedPreferences
import com.nexdecade.nd_shorts.data.PREF_NAME_ND_SHORTS
import com.nexdecade.nd_shorts.data.SessionPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Shared Preference Module Start
    @Provides
    fun providesSessionSharedPreference(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(PREF_NAME_ND_SHORTS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesPreference(pref: SharedPreferences, @ApplicationContext ctx: Context): SessionPreference {
        return SessionPreference(pref, ctx)
    }
    // Shared Preference Module End

}