package com.syfuzzaman.swipeable_video_player_android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.data.MyViewModel
import com.syfuzzaman.swipeable_video_player_android.data.Resource
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean
import com.syfuzzaman.swipeable_video_player_android.data.navigateTo
import com.syfuzzaman.swipeable_video_player_android.data.observe
import com.syfuzzaman.swipeable_video_player_android.databinding.FragmentShortsGridBinding

class ShortsGridFragment : Fragment(), BaseListItemCallback<ShortsBean> {
    private lateinit var binding: FragmentShortsGridBinding
    private val viewModel by activityViewModels<MyViewModel>()
    private lateinit var mAdapter: ShortsGridAdapter
    private var shortsList: List<ShortsBean>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShortsGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ShortsGridAdapter(this)
        with(binding.shortGridList) {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = mAdapter
            setHasFixedSize(true)
        }
        observeShortsResponse()
        viewModel.getShortsGridResponse()
    }

    private fun observeShortsResponse() {
        observe(viewModel.shortsGridResponse) {
            when (it) {
                is Resource.Success -> {
                    Log.d("SHORTS_GRID", "observeShortsResponse: ${it.data.shorts}")
                    it.data.shorts.let {bean ->
                        mAdapter.removeAll()
                        shortsList = bean
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