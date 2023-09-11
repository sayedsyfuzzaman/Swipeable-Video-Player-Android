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
import okhttp3.OkHttpClient

class PlayerViewBindingAdapter{

    companion object{
        // to hold all generated players
        private var playersMap: MutableMap<Int, ExoPlayer>  = mutableMapOf()
        // to hold current player
        private var currentPlayingVideo: Pair<Int, ExoPlayer>? = null
        fun releaseAllPlayers(){
            playersMap.map {
                it.value.release()
            }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int){
            playersMap[index]?.pause()
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

        @JvmStatic
        @SuppressLint("UnsafeOptInUsageError")
        @BindingAdapter(value = ["video_url", "on_state_change", "progressbar", "thumbnail", "item_index", "autoPlay"], requireAll = false)
        fun PlayerView.loadVideo(url: String, callback: PlayerStateCallback, progressbar: ProgressBar, thumbnail: ImageView, item_index: Int? = null, autoPlay: Boolean = false) {

            val player: ExoPlayer?
            val httpDataSourceFactory: OkHttpDataSource.Factory?

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
            httpDataSourceFactory = OkHttpDataSource.Factory(client)
            val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory)

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

            // Set an event listener to detect when the video playback ends
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
                }
            })

            if (item_index == 0){
                playIndexThenPausePreviousPlayer(0)
            }

        }
    }

}
