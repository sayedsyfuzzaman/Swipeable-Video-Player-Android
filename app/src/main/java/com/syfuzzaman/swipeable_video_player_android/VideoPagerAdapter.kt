package com.syfuzzaman.swipeable_video_player_android

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.button.MaterialButton
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import okhttp3.OkHttpClient
import java.io.File
import kotlin.math.log

@SuppressLint("UnsafeOptInUsageError")
class VideoPagerAdapter(private val videoItems: List<ShortsAPIResponse.ShortsBean>, private val context: Context, private val viewModel: MyViewModel ) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var player: ExoPlayer? = null
    private var httpDataSourceFactory: OkHttpDataSource.Factory? = null
    var simpleCache: SimpleCache
    private var currentVideoIndex: Int = 0


    init {
        val evict = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val databaseProvider: DatabaseProvider = StandaloneDatabaseProvider(context)
        simpleCache = SimpleCache(File(context.cacheDir, "media"), evict, databaseProvider)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoFrame: PlayerView = itemView.findViewById(R.id.videoFrame)
        val description: TextView = itemView.findViewById(R.id.description)
        val author: TextView = itemView.findViewById(R.id.userName)
        val subscribe: Button = itemView.findViewById(R.id.button)
        val like: ImageView = itemView.findViewById(R.id.like)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val dislike: ImageView = itemView.findViewById(R.id.dislike)
        val dislikeCount: TextView = itemView.findViewById(R.id.dislikeCount)
        val comment: ImageView = itemView.findViewById(R.id.comment)
        val share: ImageView = itemView.findViewById(R.id.share)
        val tags: TextView = itemView.findViewById(R.id.tags)
        val userProfileImage: ImageView = itemView.findViewById(R.id.userProfileImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val videoPager = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        Log.d("nd_shorts", "onCreateViewHolder Called")
        return VideoViewHolder(videoPager)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Log.d("nd_shorts", "Binding position: $position")
        val videoElement = videoItems[position]

        holder.description.text = videoElement.description
        holder.author.text = videoElement.channelName
        holder.likeCount.text = videoElement.reactions?.likes.toString() ?: ""
        holder.dislikeCount.text = videoElement.reactions?.dislikes.toString() ?: ""
        holder.tags.text = videoElement.tags.toString()

        holder.userProfileImage.load(videoElement.channelPhotoUrl){
            transformations(CircleCropTransformation())
        }

        holder.subscribe.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.like.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.dislike.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.comment.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.share.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
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
                holder.videoFrame.player = exoPlayer
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
        player?.addListener( object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        viewModel.swipeJob.value = true
                        Log.d("PAGE_SCROLL", "startPageScroll: video ends")
                    }

                }
            }
        })
    }

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d("nd_shorts", "onViewAttachedToWindow: ")
        holder.videoFrame.player?.apply {
            seekTo(0)
            playWhenReady = true
            if (this.playerError != null) {
                prepare()
            }
        }
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
        holder.videoFrame.player?.release()
        simpleCache.release()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.videoFrame.player?.pause()
        simpleCache.release()
    }

    override fun getItemCount(): Int = videoItems.size

    fun release(){
        player?.release()
        simpleCache.release()
    }

}
