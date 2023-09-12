package com.syfuzzaman.swipeable_video_player_android.data

import android.content.Context
import android.content.SharedPreferences

const val PREF_NAME_ND_SHORTS = "ND_SHORTS"
class SessionPreference (private val pref: SharedPreferences, private val context: Context){
    var autoPlayVolume: Boolean
        get() = pref.getBoolean(PREF_AUTO_PLAY_VOLUME, false)
        set(isActive) {
            pref.edit().putBoolean(PREF_AUTO_PLAY_VOLUME, isActive).apply()
        }


    companion object {
        private const val PREF_AUTO_PLAY_VOLUME = "pref_auto_play_volume"

        private var instance: SessionPreference? = null

        fun init(mContext: Context) {
            if (instance == null) {
                instance = SessionPreference(
                    mContext.getSharedPreferences(
                        PREF_NAME_ND_SHORTS,
                        Context.MODE_PRIVATE
                    ), mContext
                )
            }
        }

        fun getInstance(): SessionPreference {
            if (instance == null) {
                throw InstantiationException("Instance is null...call init() first")
            }
            return instance as SessionPreference
        }

    }
}