package com.syfuzzaman.swipeable_video_player_android

import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.common.MyBaseAdapter
import com.nexdecade.nd_shorts.data.Banner

class BannerAdapter(
    cb: BaseListItemCallback<Banner>
) : MyBaseAdapter<Banner>(cb) {
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_banner
    }
}