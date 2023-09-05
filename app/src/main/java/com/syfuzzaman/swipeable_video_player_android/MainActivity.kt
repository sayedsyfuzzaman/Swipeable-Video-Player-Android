package com.syfuzzaman.swipeable_video_player_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.widget.ViewPager2
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log

@UnstableApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val date = Date(123, 8, 6)

        if(bdTime() >= date) {
            this.finish()
        }
    }



    private fun bdTime(): Date {
        return try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"))
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Dhaka"))
            cal.time
        } catch (e: Exception) {
            Date()
        }
    }

    val currentDateTime: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            return sdf.format(bdTime()) ?: sdf.format(Date())
        }

    val currentDateTimeMillis: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)
            return sdf.format(bdTime()) ?: sdf.format(Date())
        }

    override fun onPause() {
        super.onPause()
        Log.d("nd_shorts", "onPause: sd")
    }
}