package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.nexdecade.nd_shorts.data.MyViewModel
import com.nexdecade.nd_shorts.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<MyViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = Handler(Looper.getMainLooper())
        var isScrolling = false

        binding.nestedScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            // Handle scrolling
            isScrolling = true
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                if (isScrolling) {
                    // Scrolling is still in progress, do nothing
                } else {
                    // Scrolling has stopped, check fragment visibility
                    val visibility = isFragmentFullyVisible(binding.shortsAutoPlayFragment, binding.nestedScrollView)
                    viewModel.autoPlayFragmentFullyVisible.value = visibility
                }
            }, 300) // Adjust the delay time as needed
        }

        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            isScrolling = false
        }
    }

    fun isFragmentFullyVisible(fragmentView: View, nestedScrollView: NestedScrollView): Boolean {
        val scrollY = nestedScrollView.scrollY
        val fragmentTop = fragmentView.top
        val fragmentBottom = fragmentView.bottom

        val scrollViewHeight = nestedScrollView.height
        val scrollViewBottom = scrollY + scrollViewHeight

        // Check if the entire height of the fragment is visible
        return fragmentTop >= scrollY && fragmentBottom <= scrollViewBottom
    }





}