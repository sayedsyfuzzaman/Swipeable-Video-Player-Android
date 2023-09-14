package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.utils.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShortsFragment : Fragment() {

    private lateinit var binding: FragmentShortsBinding
    private var videoPagerAdapter: VideoPagerAdapter? = null
    private val viewModel by activityViewModels<MyViewModel>()
    private var selectedPosition = 0
    private var shortsList: List<ShortsBean>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShortsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        selectedPosition = arguments?.getInt("selectedPosition", 0) ?: 0
        shortsList = arguments?.getParcelableArrayList("shorts")

        Log.d("last_item_play_issue", "onViewCreated: $shortsList")

        shortsList?.let {
            loadAdapter(it)
        } ?: run {
            observeShortsResponse()
            viewModel.getShortsResponse()
        }

        binding.menu.setOnClickListener {
            Toast.makeText(requireContext(), "Action Required!", Toast.LENGTH_SHORT).show()
        }
        binding.search.setOnClickListener {
            Toast.makeText(requireContext(), "Action Required!", Toast.LENGTH_SHORT).show()
        }
        


    }

    private fun viewPagerPageChangedCallback(listSize: Int) = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            videoPagerAdapter?.startPlaying(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)

            Log.d(TAG, "onPageScrollStateChanged: $listSize")
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                when (binding.viewPager.currentItem) {
                    listSize-1 -> binding.viewPager.setCurrentItem(0, false)
                }
            }
        }
    }

    private fun observeShortsResponse() {
        observe(viewModel.shortsResponse) {
            when (it) {
                is Resource.Success -> {
                    Log.d("ND_SHORTS", "observeShortsResponse: ${it.data.shorts}")
                    loadAdapter(it.data.shorts.asReversed())
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.error.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadAdapter(shorts: List<ShortsBean>) {
        videoPagerAdapter = VideoPagerAdapter(
            requireContext(),
            viewModel,
            shorts,
            this
        )
        binding.viewPager.registerOnPageChangeCallback(viewPagerPageChangedCallback(shorts.size + 1))
        binding.viewPager.adapter = videoPagerAdapter
        binding.viewPager.currentItem = 0
        scrollToNext()
    }

    private fun scrollToNext() {
        observe(viewModel.swipeJob) {
            Log.d("PAGE_SCROLL", "startPageScroll: call")
            if (it) {
                if ((videoPagerAdapter?.itemCount ?: 0) > 0) {
                    binding.viewPager.currentItem =
                        (binding.viewPager.currentItem + 1) % videoPagerAdapter!!.itemCount
                    Log.d("PAGE_SCROLL", "startPageScroll: scrolled")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPagerAdapter?.release()
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerPageChangedCallback(0))
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
        videoPagerAdapter?.pause(binding.viewPager.currentItem)
    }
}