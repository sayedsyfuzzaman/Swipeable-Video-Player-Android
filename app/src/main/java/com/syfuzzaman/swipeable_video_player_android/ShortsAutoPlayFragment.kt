package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.data.navigateTo
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsAutoPlayBinding

class ShortsAutoPlayFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentShortsAutoPlayBinding
    private val viewModel by activityViewModels<MyViewModel>()
    private lateinit var mAdapter: ShortsAutoPlayAdapter
    private var shortsList: List<ShortsBean>? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShortsAutoPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ShortsAutoPlayAdapter(this)
        with(binding.rvShortsAutoPlay) {
            adapter = mAdapter
        }
        observeShortsResponse()
        viewModel.getShortsAutoPlayResponse()
    }

    private fun observeShortsResponse() {
        observe(viewModel.shortsAutoPlayResponse) {
            when (it) {
                is Resource.Success -> {
                    it.data.shorts.let {bean ->
                        mAdapter.removeAll()
                        shortsList = bean.shuffled()
                        mAdapter.addAll(shortsList!!)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        super.onItemClick(position)
        shortsList?.let {
            val playingShortsList = it.subList(position, it.size).toMutableList()
            if (position > 0){
                playingShortsList.addAll(it.subList(0, position))
            }
            findNavController().navigateTo(resId = R.id.shortsFragment, args = bundleOf("shorts" to playingShortsList))
        }
    }
}