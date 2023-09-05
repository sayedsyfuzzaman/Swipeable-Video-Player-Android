package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import com.syfuzzaman.swipeable_video_player_android.data.navigateTo
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsAutoPlayBinding

class ShortsAutoPlayFragment : Fragment(), BaseListItemCallback<ShortsAPIResponse.ShortsBean> {
    private lateinit var binding: FragmentShortsAutoPlayBinding
    private val viewModel by activityViewModels<MyViewModel>()
    private lateinit var mAdapter: ShortsAutoPlayAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                        mAdapter.addAll(bean.asReversed())
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemClicked(item: ShortsAPIResponse.ShortsBean) {
        super.onItemClicked(item)
        findNavController().navigateTo(R.id.shortsFragment)
    }
}