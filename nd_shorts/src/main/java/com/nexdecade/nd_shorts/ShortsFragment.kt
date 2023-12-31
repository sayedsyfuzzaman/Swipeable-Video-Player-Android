package com.nexdecade.nd_shorts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.nexdecade.nd_shorts.data.MyViewModel
import com.nexdecade.nd_shorts.data.Resource
import com.nexdecade.nd_shorts.data.ShortsBean
import com.nexdecade.nd_shorts.databinding.FragmentShortsBinding
import com.nexdecade.nd_shorts.utils.observe

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
            videoPagerAdapter?.startPlaying(position, videoPagerAdapter?.positionWiseContentId?.get(position) ?: 0)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
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
                    loadAdapter(it.data.shorts)
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
            if (it) {
                if ((videoPagerAdapter?.itemCount ?: 0) > 0) {
                    binding.viewPager.currentItem = (binding.viewPager.currentItem + 1) % videoPagerAdapter!!.itemCount
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