package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsBinding

class ShortsFragment : Fragment() {

    private lateinit var binding: FragmentShortsBinding
    private var videoPagerAdapter: VideoPagerAdapter? = null
    private val viewModel by activityViewModels<MyViewModel>()
    private var selectedPosition = 0
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShortsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedPosition = arguments?.getInt("selectedPosition", 0) ?: 0
        observeShortsResponse()
        viewModel.getShortsResponse()

    }

    private val viewPagerPageChangedCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            videoPagerAdapter?.startPlaying(position)
        }
    }
    
    private fun observeShortsResponse() {
        observe(viewModel.shortsResponse) {
            when (it) {
                is Resource.Success -> {
                    Log.d("ND_SHORTS", "observeShortsResponse: ${it.data.shorts}")
                    videoPagerAdapter = VideoPagerAdapter(requireContext(), viewModel, it.data.shorts.asReversed().subList(selectedPosition, it.data.shorts.asReversed().size))
                    binding.viewPager.registerOnPageChangeCallback(viewPagerPageChangedCallback)
                    binding.viewPager.adapter = videoPagerAdapter
                    scrollToNext()
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun scrollToNext() {
        observe(viewModel.swipeJob){
            Log.d("PAGE_SCROLL", "startPageScroll: call")
            if (it){
                if ((videoPagerAdapter?.itemCount ?: 0) > 0) {
                    binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % videoPagerAdapter!!.itemCount
                    Log.d("PAGE_SCROLL", "startPageScroll: scrolled")

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        videoPagerAdapter?.release()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerPageChangedCallback)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        videoPagerAdapter?.release()
    }

    override fun onResume() {
        super.onResume()
        videoPagerAdapter?.resume(binding.viewPager.currentItem)
    }
    
    override fun onPause() {
        super.onPause()
        videoPagerAdapter?.pause()
    }
}