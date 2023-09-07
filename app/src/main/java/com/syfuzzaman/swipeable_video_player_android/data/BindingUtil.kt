package com.syfuzzaman.swipeable_video_player_android.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.syfuzzaman.swipeable_video_player_android.R
import javax.inject.Singleton

@Singleton
class BindingUtil {
    companion object{
        @JvmStatic
        @BindingAdapter(value = ["loadImageFromUrl", "maintainRatio"], requireAll = false)
        fun bindImageFromUrl(view: ImageView, imageUrl: String?, maintainRatio: Boolean = true) {
            view.load(imageUrl){
                crossfade(true)
                placeholder(R.drawable.poster_placeholder)
                scale(Scale.FILL)
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["loadImageFromResource", "maintainRatio"], requireAll = false)
        fun bindImageFromResource(view: ImageView, imageResource: Int?, maintainRatio: Boolean = true) {
            view.setImageResource(imageResource ?: R.drawable.poster1)
        }
    }

}