package com.syfuzzaman.swipeable_video_player_android

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.DefaultTimeBar
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.databinding.ItemVideoBinding
import okhttp3.OkHttpClient
import java.io.File
import kotlin.random.Random

@SuppressLint("UnsafeOptInUsageError")
class VideoPagerAdapter(
    private val context: Context,
    private val viewModel: MyViewModel,
    private val videoItems: List<ShortsBean>,
) : RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var player: ExoPlayer? = null
    private var httpDataSourceFactory: OkHttpDataSource.Factory? = null
    var simpleCache: SimpleCache
    var hashMap: HashMap<Int, VideoViewHolder> = HashMap()
    var exoBuffering: ProgressBar? = null

    private val newList: List<ShortsBean> =
        listOf(videoItems.last()) + videoItems + listOf(videoItems.first())

    init {
        val evict = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val databaseProvider: DatabaseProvider = StandaloneDatabaseProvider(context)
        simpleCache = SimpleCache(File(context.cacheDir, "media"), evict, databaseProvider)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemVideoBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val videoPager = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        Log.d("nd_shorts", "onCreateViewHolder Called")
        return VideoViewHolder(videoPager)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        with(holder.binding) {
            Log.d("item_play_issue", "Binding position: $position")
            Log.d("item_play_issue", "onBindViewHolder: $newList")

            hashMap.put(position, holder)

            var videoElement = ShortsBean()

            Log.d("crash_issue_infscroll", "bindpos: ${holder.bindingAdapterPosition}, size: ${newList.size}")
            if ((holder.bindingAdapterPosition) == newList.size-1){
                videoElement = newList[0]
            }else{
                videoElement = newList[holder.bindingAdapterPosition+1]
            }

            val playPauseButton = videoFrame.findViewById<ImageView>(R.id.play_pause)
            val progressBar = videoFrame.findViewById<DefaultTimeBar>(R.id.exo_progress)
            exoBuffering = videoFrame.findViewById<ProgressBar>(R.id.exo_buffering)
            
            description.text = videoElement.description
            userName.text = videoElement.channelName
            likeCount.text = Random.nextInt(100, 1000).toString() //videoElement.reactions?.likes.toString() ?: ""
            dislikeCount.text = Random.nextInt(100, 1000).toString() //videoElement.reactions?.dislikes.toString() ?: ""
            tags.text = videoElement.tags.toString()

            userProfileImage.load(videoElement.channelPhotoUrl) {
                transformations(CircleCropTransformation())
            }

            subscribeButton.setOnClickListener {
                Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
            }
            like.setOnClickListener {
                Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
            }
            dislike.setOnClickListener {
                Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
            }
            comment.setOnClickListener {
                Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
            }
            share.setOnClickListener {
                Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
            }
            gestureOverlay.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in_anim)
                
                if (videoFrame.player?.isPlaying == true) {
                    videoFrame.player?.pause()
                    playPauseButton.setImageResource(R.drawable.ic_pause)
                    playPauseButton.isVisible = true
                    playPauseButton.startAnimation(animation)
                    progressBar.showScrubber(0)
                } else {
                    videoFrame.player?.play()
                    playPauseButton.setImageResource(R.drawable.ic_play_p)
                    playPauseButton.isVisible = true
                    playPauseButton.startAnimation(animation)
                    progressBar.hideScrubber(0)
                }
            }
            
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
            val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory!!)

            val trackSelector = DefaultTrackSelector(context).apply {
                setParameters(buildUponParameters().setMaxVideoSizeSd())
            }
            player = ExoPlayer.Builder(context)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
                .also { exoPlayer ->
                    videoFrame.player = exoPlayer
//                val mediaItem = MediaItem.fromUri(Uri.parse(videoElement.sources))
                    val mediaItem = MediaItem.Builder()
                        .setUri(videoElement.mediaUrl)
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .build()
                    val httpDataSourceFactory =
                        DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                    val defaultDataSourceFactory =
                        DefaultDataSourceFactory(context, httpDataSourceFactory)
                    val cacheDataSourceFactory = CacheDataSource.Factory()
                        .setCache(simpleCache)
                        .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
                    val mediaSource = ProgressiveMediaSource
                        .Factory(cacheDataSourceFactory)
                        .createMediaSource(mediaItem)
                    exoPlayer.setMediaSource(mediaSource)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady = false
                    exoPlayer.prepare()
                }

            // Set an event listener to detect when the video playback ends
            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            exoBuffering?.visibility = View.GONE
                            viewModel.swipeJob.value = holder.bindingAdapterPosition < videoItems.size-1
                            Log.d("PAGE_SCROLL", "startPageScroll: video ends")
                        }
                        Player.STATE_BUFFERING -> {
                            exoBuffering?.visibility = View.VISIBLE
                            viewModel.swipeJob.value = false
                        }
                        else -> {
                            exoBuffering?.visibility = View.GONE
                            viewModel.swipeJob.value = false
                        }

                    }
                }
            })
        }
    }
    
    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("nd_shorts", "onViewAttachedToWindow: ")
    }
    
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Log.d("nd_shorts", "Player Released onDetach")
        player?.release()
        simpleCache.release()
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        Log.d("nd_shorts", "View recycled")
        holder.binding.videoFrame.player?.release()
        simpleCache.release()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.videoFrame.player?.pause()
        simpleCache.release()
    }

    override fun getItemCount(): Int = newList.size

    fun startPlaying(position: Int) {
        pause()
        hashMap[position]?.binding?.videoFrame?.findViewById<ImageView>(R.id.play_pause)?.isVisible = false
        hashMap[position]?.binding?.videoFrame?.findViewById<DefaultTimeBar>(R.id.exo_progress)?.hideScrubber(0)
        hashMap[position]?.binding?.videoFrame?.player?.apply {
            seekTo(0)
            playWhenReady = true
            if (this.playerError != null) {
                prepare()
            }
        }
    }

    /* To stop the playing videos if any on activity closing,
    we need to get the playing position in adapter and
    stop the player and remove player view of that view-holder.
    */
    fun release() {
        player?.release()
        simpleCache.release()

        for (key in hashMap.keys) {
            hashMap[key]?.binding?.videoFrame?.player?.release()
        }
    }

    fun pause() {
        player?.pause()
        for (key in hashMap.keys) {
            hashMap[key]?.binding?.videoFrame?.player?.pause()
        }
    }

    fun resume(currentItem: Int) {
        hashMap[currentItem]?.binding?.videoFrame?.player?.play()
    }
}
