package com.syfuzzaman.swipeable_video_player_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.viewpager2.widget.ViewPager2
import com.syfuzzaman.swipeable_video_player_android.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

@UnstableApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var videoList: VideoList
    private lateinit var videoPagerAdapter: VideoPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewPager = findViewById(R.id.viewPager)
        loadVideoData()

        videoPagerAdapter = VideoPagerAdapter(videoList.videos, this)
        viewPager.adapter = videoPagerAdapter
    }


    private fun loadVideoData() {
        videoList = VideoList(
            listOf(
                VideoBean(
                    description = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/1.mp4/playlist.m3u8",
                    subtitle = "By Blender Foundation",
                    thumb = "images/BigBuckBunny.jpg",
                    title = "Big Buck Bunny"
                ),
                VideoBean(
                    description = "The first Blender Open Movie from 2006",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/2.mp4/playlist.m3u8",
                    subtitle = "By Blender Foundation",
                    thumb = "images/ElephantsDream.jpg",
                    title = "Elephant Dream"
                ),
                VideoBean(
                    description = "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes.",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/3.mp4/playlist.m3u8",
                    subtitle = "By Google",
                    thumb = "images/ForBiggerBlazes.jpg",
                    title = "For Bigger Blazes"
                ),
                VideoBean(
                    description = "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when Batman's escapes aren't quite big enough.",
                    sources = "https://iptv-isp.nexdecade.com/vod/shorts/clip/4.mp4/playlist.m3u8",
                    subtitle = "By Google",
                    thumb = "images/ForBiggerEscapes.jpg",
                    title = "For Bigger Escap"
                )
            )
        )
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
    }

}