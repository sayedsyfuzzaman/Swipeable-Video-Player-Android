package com.nexdecade.nd_shorts.common

import androidx.fragment.app.Fragment
import com.nexdecade.nd_shorts.data.SessionPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    @Inject lateinit var mPref: SessionPreference
}