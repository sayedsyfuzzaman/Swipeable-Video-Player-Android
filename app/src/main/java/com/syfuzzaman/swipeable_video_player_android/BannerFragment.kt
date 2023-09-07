package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.Banner
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentBannerBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BannerFragment : Fragment() , BaseListItemCallback<Banner>{
    private lateinit var binding: FragmentBannerBinding
    private var slideJob: Job? = null
    private val viewModel by activityViewModels<MyViewModel>()
    private lateinit var mAdapter: BannerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.featuredJob?.cancel()
        viewModel.featuredJob = null

        mAdapter = BannerAdapter(this)
        binding.bannerViewPager.adapter = mAdapter

        TabLayoutMediator(
            binding.bannerIndicator,
            binding.bannerViewPager,
            true
        ) { _, _ -> }.attach()

        val bannerList: List<Banner> = listOf(
            Banner(R.drawable.poster1),
            Banner(R.drawable.poster2),
            Banner(R.drawable.poster3)
        )

        mAdapter.removeAll()
        mAdapter.addAll(bannerList)
        startPageScroll()
        binding.bannerViewPager.visibility = View.VISIBLE
    }

    private fun startPageScroll() {
        slideJob?.cancel()
        slideJob = lifecycleScope.launch {
            while (isActive) {
                delay(5000)
                if (isActive && mAdapter.itemCount > 0) {
                    binding.bannerViewPager.currentItem =
                        (binding.bannerViewPager.currentItem + 1) % mAdapter.itemCount
                }
            }
        }
    }

    override fun onDestroyView() {
        slideJob?.cancel()
        binding.bannerViewPager.adapter = null
        super.onDestroyView()
    }

}