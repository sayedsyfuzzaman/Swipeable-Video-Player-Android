package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syfuzzaman.swipeable_video_player_android.auto_play.AutoPlayAdapter
import com.syfuzzaman.swipeable_video_player_android.auto_play.PlayerViewBindingAdapter.Companion.playIndexThenPausePreviousPlayer
import com.syfuzzaman.swipeable_video_player_android.auto_play.PlayerViewBindingAdapter.Companion.releaseAllPlayers
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.utils.navigateTo
import com.syfuzzaman.swipeable_video_player_android.utils.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsAutoPlayBinding

class ShortsAutoPlayFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentShortsAutoPlayBinding
    private val viewModel by activityViewModels<MyViewModel>()
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var mAdapter: AutoPlayAdapter

    private var firstVisibleItem = 0
    private var visibleItemCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShortsAutoPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = AutoPlayAdapter(requireContext())
        with(binding.rvShortsAutoPlay) {
            adapter = mAdapter
            setHasFixedSize(true)
        }



        binding.rvShortsAutoPlay.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(recyclerView.scrollState){
                    RecyclerView.SCROLL_STATE_IDLE ->{
                        val manager = recyclerView.layoutManager
                        require(manager is LinearLayoutManager) { "Expected recyclerview to have linear layout manager" }
                        val mLayoutManager = manager
                        visibleItemCount = mLayoutManager.childCount
                        firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                        if (firstVisibleItem!=0){
                            playIndexThenPausePreviousPlayer(firstVisibleItem)
                        }
                    }
                }
            }
        })

        mAdapter.SetOnItemClickListener(object : AutoPlayAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: ShortsBean?) {
                homeViewModel.autoPlayShorts.value?.let {
                    val playingShortsList = it.subList(position, it.size).toMutableList()
                    if (position > 0){
                        playingShortsList.addAll(it.subList(0, position))
                    }
                    findNavController().navigateTo(resId = R.id.shortsFragment, args = bundleOf("shorts" to playingShortsList))
                }
            }
        })

        if (homeViewModel.autoPlayShorts.value?.isNotEmpty() == true) {
            mAdapter.removeAll()
            mAdapter.addAll(homeViewModel.autoPlayShorts.value!!)
        } else {
            observeShortsResponse()
            viewModel.getShortsAutoPlayResponse()
        }
    }

    private fun observeShortsResponse() {
        observe(viewModel.shortsAutoPlayResponse) {
            when (it) {
                is Resource.Success -> {
                    it.data.shorts.let {bean ->
                        mAdapter.removeAll()
                        homeViewModel.autoPlayShorts.value = bean.shuffled()
                        mAdapter.addAll(homeViewModel.autoPlayShorts.value!!)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        releaseAllPlayers()
    }
}