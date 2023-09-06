package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}