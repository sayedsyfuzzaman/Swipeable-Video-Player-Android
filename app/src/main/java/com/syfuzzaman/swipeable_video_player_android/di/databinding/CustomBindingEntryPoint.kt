package com.syfuzzaman.swipeable_video_player_android.di.databinding

import androidx.databinding.DataBindingComponent
import com.syfuzzaman.swipeable_video_player_android.auto_play.PlayerViewBindingAdapter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@EntryPoint
@BindingScoped
@InstallIn(CustomBindingComponent::class)
interface CustomBindingEntryPoint : DataBindingComponent {

    @BindingScoped
    override fun getPlayerViewBindingAdapter(): PlayerViewBindingAdapter
}
