package com.syfuzzaman.swipeable_video_player_android

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
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
import com.google.android.material.button.MaterialButton
import okhttp3.OkHttpClient
import java.io.File

@SuppressLint("UnsafeOptInUsageError")
class VideoPagerAdapter(private val videoItems: List<VideoBean>, private val context: Context) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var player: ExoPlayer? = null
    private var holder: VideoViewHolder? = null
    private var httpDataSourceFactory: OkHttpDataSource.Factory? = null
    private var simpleCache: SimpleCache

    init {
        val evict = LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong())
        val databaseProvider: DatabaseProvider = StandaloneDatabaseProvider(context)
        simpleCache = SimpleCache(File(context.cacheDir, "media"), evict, databaseProvider)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoFrame: PlayerView = itemView.findViewById(R.id.videoFrame)
        val description: TextView = itemView.findViewById(R.id.description)
        val author: TextView = itemView.findViewById(R.id.userName)
        val subscribe: MaterialButton = itemView.findViewById(R.id.button)
        val search: ImageView = itemView.findViewById(R.id.search)
        val menu: ImageView = itemView.findViewById(R.id.menu)
        val like: ImageView = itemView.findViewById(R.id.like)
        val dislike: ImageView = itemView.findViewById(R.id.dislike)
        val comment: ImageView = itemView.findViewById(R.id.comment)
        val share: ImageView = itemView.findViewById(R.id.share)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val videoPager = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        Log.d("nex_shorts", "onCreateViewHolder Called")
        return VideoViewHolder(videoPager)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Log.d("nex_shorts", "Binding position: $position")
        this.holder = holder
        val videoElement = videoItems[position]

        holder.description.text = videoElement.description
        holder.author.text = videoElement.subtitle

        holder.subscribe.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.search.setOnClickListener {
            Toast.makeText(context, "Action Required!", Toast.LENGTH_SHORT).show()
        }
        holder.menu.setOnClickListener {
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
                    .header("Cookie",  "Edge-Cache-Cookie=URLPrefix=aHR0cHM6Ly92b2RtcHJvZC1jZG4udG9mZmVlbGl2ZS5jb20v:Expires=1717147185:KeyName=prod_vod:Signature=SuLzgbQdZPy7lRNYqkSYEBTklI-YYQvdZ7rH1RUkKP-LRv9bWNxkUdpBDnEmdaCbg8tlmTwvcR3a8oHj3xhsAA")
                    .build()
                chain.proceed(modifiedRequest)
            }
            .build()

        // Create a HttpDataSourceFactory with the custom OkHttpClient
        httpDataSourceFactory = OkHttpDataSource.Factory(client)

        val mediaSourceFactory = DefaultMediaSourceFactory(httpDataSourceFactory!!)

//        player = ExoPlayer.Builder(context)
//            .setTrackSelector(trackSelector)
//            .setMediaSourceFactory(mediaSourceFactory)
//            .build()
//            .also { exoPlayer ->
//                holder.videoFrame.player = exoPlayer
//                val mediaItem = MediaItem.Builder()
//                    .setUri(videoElement.sources)
//                    .setMimeType(MimeTypes.APPLICATION_M3U8)
//                    .build()
//                exoPlayer.setMediaItem(mediaItem)
//                exoPlayer.playWhenReady = false
//                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
//                exoPlayer.prepare()
//                exoPlayer.play()
//            }

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
                    .setUri(videoElement.sources)
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
                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                exoPlayer.prepare()
            }
    }

    override fun onViewAttachedToWindow(holder: VideoViewHolder) {
        super.onViewAttachedToWindow(holder)
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
        Log.d("Yt Shorts", "Player Released onDetach")
        player?.release()
        simpleCache.release()
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        Log.d("YT Shorts", "View recycled")
        holder.videoFrame.player?.release()
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.videoFrame.player?.pause()
    }

    override fun getItemCount(): Int = videoItems.size

    fun pause(){
        this.holder?.videoFrame?.player?.pause()
    }
    fun play(){
        this.holder?.videoFrame?.player?.play()

    }
}
