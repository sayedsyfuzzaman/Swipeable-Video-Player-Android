package com.syfuzzaman.swipeable_video_player_android.auto_play

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.syfuzzaman.swipeable_video_player_android.common.PlayerStateCallback
import com.syfuzzaman.swipeable_video_player_android.data.SessionPreference
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerViewBindingAdapter  @Inject constructor(private val mPref: SessionPreference){

    @SuppressLint("UnsafeOptInUsageError")
    @BindingAdapter(value = ["video_url", "on_state_change", "progressbar", "thumbnail", "item_index", "autoPlay", "volumeControl"], requireAll = false)
    fun PlayerView.loadVideo(url: String, callback: PlayerStateCallback, progressbar: ProgressBar, thumbnail: ImageView, item_index: Int? = null, autoPlay: Boolean = false, volumeControl : ImageView) {

        val player: ExoPlayer?
        val okHttpDataSourceFactory: OkHttpDataSource.Factory?

        thumbnail.visibility = View.VISIBLE
        progressbar.visibility = View.GONE

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
//                    .header("Cookie",  "")
                    .build()
                chain.proceed(modifiedRequest)
            }
            .build()

        // Create a HttpDataSourceFactory with the custom OkHttpClient
        okHttpDataSourceFactory = OkHttpDataSource.Factory(client)
        val mediaSourceFactory = DefaultMediaSourceFactory(okHttpDataSourceFactory)

        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .also { exoPlayer ->
                setKeepContentOnPlayerReset(true)
                this.player = exoPlayer
                val mediaItem = MediaItem.Builder()
                    .setUri(url)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()
                val httpDataSourceFactory =
                    DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                val defaultDataSourceFactory =
                    DefaultDataSourceFactory(context, httpDataSourceFactory)
                val mediaSource = ProgressiveMediaSource
                    .Factory(defaultDataSourceFactory)
                    .createMediaSource(mediaItem)
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = autoPlay
                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_OFF
                (this.player as ExoPlayer).volume = 0f
                this.useController = false
            }

        // add player with its index to map
        if (playersMap.containsKey(item_index))
            playersMap.remove(item_index)
        if (item_index != null)
            playersMap[item_index] = player


        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    callback.onVideoBuffering(player)
                    thumbnail.visibility = View.VISIBLE
                    progressbar.visibility = View.VISIBLE
                }
                if (playbackState == ExoPlayer.STATE_READY) {
                    // fetched the video duration
                    progressbar.visibility = View.GONE
                    thumbnail.visibility = View.GONE
                    callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                }
                if (playbackState == Player.STATE_READY && player.playWhenReady){
                    // started playing/resumed the video
                    callback.onStartedPlaying(player)
                }
                if(playbackState == Player.STATE_ENDED || playbackState == Player.STATE_IDLE){
                    progressbar.visibility = View.GONE
                    thumbnail.visibility = View.VISIBLE
                }
            }
        })

    }
    companion object{
        private var playersMap: MutableMap <Int, ExoPlayer>  = mutableMapOf()
        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null

        fun releaseAllPlayers(){
            playersMap.map {
                it.value.release()
            }
        }

        fun pauseAllPlayers(){
            playersMap.map {
                it.value.pause()
            }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int){
            playersMap[index]?.release()
            playersMap.remove(index)
        }

        // call when scroll to pause any playing player
        private fun pauseCurrentPlayingVideo(){
            if (currentPlayingVideo != null){
                currentPlayingVideo?.second?.playWhenReady = false
                currentPlayingVideo?.second?.pause()
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int){
            if (playersMap[index]?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap[index]?.prepare()
                playersMap[index]?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap[index]!!)
            }
        }
    }
}
