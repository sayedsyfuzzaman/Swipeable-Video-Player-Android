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
    private lateinit var viewPager: ViewPager2
    private lateinit var videoList: VideoList
    private var videoPagerAdapter: VideoPagerAdapter? = null
    private val viewModel by viewModels<MyViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val date = Date(123, 8, 6)

        if(bdTime() >= date) {
            this.finish()
        }

        viewPager = findViewById(R.id.viewPager)
//        loadVideoData()
        observeShortsResponse()
        viewModel.getShortsResponse()

    }

    private fun observeShortsResponse(){
        observe(viewModel.shortsResponse){
            when(it){
                is Resource.Success ->{
                    Log.d("ND_SHORTS", "observeShortsResponse: ${it.data.shorts}")
                    videoPagerAdapter = VideoPagerAdapter(it.data.shorts.asReversed(), this)
                    viewPager.adapter = videoPagerAdapter
                }
                is Resource.Failure ->{
                    Toast.makeText(this, it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
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


    private fun loadVideoData() {
        videoList = VideoList(
            listOf(
                VideoBean(
                    description = "Speed and Precision | Nascar Pit Stop",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/1.mp4/playlist.m3u8",
                    subtitle = "ohn Hunter Nemechek",
                    thumb = "images/BigBuckBunny.jpg",
                    title = "Big Buck Bunny"
                ),
                VideoBean(
                    description = "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes.",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/2.mp4/playlist.m3u8",
                    subtitle = "John Hunter Nemeche",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "Daytona ready",
                    sources = "https://vodmprod-cdn.toffeelive.com/9becd818ede2eb9a09a4a04f74ea1460/manifest.m3u8",
//                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/3.mp4/playlist.m3u8",
                    subtitle = "John Hunter Nemeche",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "Daytona ready",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/3.mp4/playlist.m3u8",
                    subtitle = "John Hunter Nemeche",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "NASCAR race pit stop",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/4.mp4/playlist.m3u8",
                    subtitle = "John Hunter Nemeche",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "Bus stop fast",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/5.mp4/playlist.m3u8",
                    subtitle = "John Hunter Nemeche",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes.",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/6.mp4/playlist.m3u8",
                    subtitle = "Google",
                    thumb = "images/ForBiggerBlazes.jpg",
                    title = "For Bigger Blazes"
                ),
                VideoBean(
                    description = "NASCAR Victory Burnout",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/7.mp4/playlist.m3u8",
                    subtitle = "Google",
                    thumb = "images/ForBiggerEscapes.jpg",
                    title = "For Bigger Escap"
                ),
                VideoBean(
                    description = "NASCAR Victory Burnout",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/8.mp4/playlist.m3u8",
                    subtitle = "Google",
                    thumb = "images/ForBiggerEscapes.jpg",
                    title = "For Bigger Escap"
                )

            )
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d("nd_shorts", "onPause: sd")
        videoPagerAdapter?.pause()
    }
}