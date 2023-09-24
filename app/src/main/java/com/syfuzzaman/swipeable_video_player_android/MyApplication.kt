package com.syfuzzaman.swipeable_video_player_android

import android.app.Application
import androidx.databinding.DataBindingUtil
import com.nexdecade.nd_shorts.data.SessionPreference
import dagger.hilt.EntryPoints
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class MyApplication : Application(){
    @Inject
//    lateinit var bindingComponentProvider: Provider<CustomBindingComponentBuilder>

    override fun onCreate() {
        super.onCreate()

        // https://gist.github.com/nuhkoca/1bf28190dc71b00a2f32ce425f99924d
//        val dataBindingComponent = bindingComponentProvider.get().build()
//        val dataBindingEntryPoint = EntryPoints.get(
//            dataBindingComponent, CustomBindingEntryPoint::class.java
//        )
//        DataBindingUtil.setDefaultComponent(dataBindingEntryPoint)
//
//        SessionPreference.init(this)
    }
}