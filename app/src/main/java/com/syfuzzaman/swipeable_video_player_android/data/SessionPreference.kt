package com.syfuzzaman.swipeable_video_player_android.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

const val PREF_NAME_ND_SHORTS = "ND_SHORTS"
class SessionPreference (private val pref: SharedPreferences, private val context: Context){

    val autoPlayWithVolumeLiveData = MutableLiveData<Boolean>()

    companion object {
//        private const val PREF_AUTO_PLAY_VOLUME = "pref_auto_play_volume"

        @SuppressLint("StaticFieldLeak")
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