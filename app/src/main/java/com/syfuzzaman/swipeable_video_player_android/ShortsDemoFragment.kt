package com.syfuzzaman.swipeable_video_player_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.findNavController
import com.nexdecade.nd_shorts.ShortsActivity
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.nexdecade.nd_shorts.data.MyViewModel
import com.nexdecade.nd_shorts.data.Resource
import com.nexdecade.nd_shorts.data.ShortsBean
import com.nexdecade.nd_shorts.utils.navigateTo
import com.nexdecade.nd_shorts.utils.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsDemoBinding

class ShortsDemoFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentShortsDemoBinding
    private val viewModel by activityViewModels<MyViewModel>()
    private lateinit var mAdapter: ShortsDemoAdapter
    private var shortsList: List<ShortsBean>? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShortsDemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ShortsDemoAdapter(this)
        with(binding.rvHorizontalShorts) {
            adapter = mAdapter
        }
        observeShortsResponse()
        viewModel.getShortsResponse()
    }

    private fun observeShortsResponse() {
        observe(viewModel.shortsResponse) {
            when (it) {
                is Resource.Success -> {
                    it.data.shorts.let {bean ->
                        mAdapter.removeAll()
                        shortsList = bean.asReversed()
                        mAdapter.addAll(shortsList!!)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @UnstableApi
    override fun onItemClick(position: Int) {
        super.onItemClick(position)
        shortsList?.let {
            val playingShortsList = it.subList(position, it.size).toMutableList()
            Log.d("item_play_issue", "position: $position  size: ${it.size}")
            Log.d("item_play_issue", "onItemClick: $playingShortsList")
            if (position > 0){
                playingShortsList.addAll(it.subList(0, position))
            }
//            findNavController().navigateTo(R.id.shortsFragment, bundleOf("shorts" to playingShortsList))
            val intent = Intent(requireContext(), ShortsActivity::class.java)
            intent.putExtra("shortsList", ArrayList(playingShortsList)) // Convert to ArrayList

            startActivity(intent)
        }

    }
}