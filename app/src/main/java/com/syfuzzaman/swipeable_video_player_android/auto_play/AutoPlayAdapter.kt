package com.syfuzzaman.swipeable_video_player_android.auto_play

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.syfuzzaman.swipeable_video_player_android.R
import com.syfuzzaman.swipeable_video_player_android.auto_play.PlayerViewBindingAdapter.Companion.releaseRecycledPlayers
import com.syfuzzaman.swipeable_video_player_android.common.PlayerStateCallback
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.databinding.ItemVideoAutoplayBinding

class AutoPlayAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {

    private var modelList: MutableList<ShortsBean> = mutableListOf()

    private var mItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VideoPlayerViewHolder {
        val binding: ItemVideoAutoplayBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_video_autoplay, viewGroup, false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoPlayerViewHolder) {
            val model = getItem(position)
            holder.onBind(model)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.bindingAdapterPosition
        releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    private fun getItem(position: Int): ShortsBean {
        return modelList[position]
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, model: ShortsBean?)
    }

    inner class VideoPlayerViewHolder(private val binding: ItemVideoAutoplayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: ShortsBean) {
            binding.root.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    it,
                    bindingAdapterPosition,
                    model
                )
            }

            binding.root.findViewById<ImageView>(R.id.volumeControl).setOnClickListener {
                val player = binding.root.findViewById<PlayerView>(R.id.item_video_exoplayer).player
                if (player?.volume == 0f){
                    player.volume = 1f
                    binding.root.findViewById<ImageView>(R.id.volumeControl).setImageResource(R.drawable.ic_unmute)
                }
                else{
                    player?.volume = 0f
                    binding.root.findViewById<ImageView>(R.id.volumeControl).setImageResource(R.drawable.ic_mute)
                }
            }

            binding.apply {
                data = model
                playerCallback = this@AutoPlayAdapter
                index = bindingAdapterPosition
                executePendingBindings()
            }


        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val player = holder.itemView.findViewById<PlayerView>(R.id.item_video_exoplayer).player
        player?.volume = 0f
        holder.itemView.findViewById<ImageView>(R.id.volumeControl).setImageResource(R.drawable.ic_mute)
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        Log.d("playvideo", "onVideoDurationRetrieved: $duration")
    }
    override fun onVideoBuffering(player: Player) {}
    override fun onStartedPlaying(player: Player) {
        Log.d("playvideo", "staaaart" + player.contentDuration)
    }
    override fun onFinishedPlaying(player: Player) {}

    fun addAll(items: List<ShortsBean>) {
        val positionStart = modelList.size
        modelList.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }
    fun removeAll()  {
        modelList.clear()
        notifyDataSetChanged()
    }

    fun updateList(modelList: MutableList<ShortsBean>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }
}