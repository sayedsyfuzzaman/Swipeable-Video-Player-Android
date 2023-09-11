package com.syfuzzaman.swipeable_video_player_android

import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.common.MyBaseAdapter
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse
import com.syfuzzaman.swipeable_video_player_android.data.ShortsBean


class ShortsAutoPlayAdapter(
    cb: BaseListItemCallback<ShortsBean>
): MyBaseAdapter<ShortsBean>(cb){
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_video_autoplay
    }
}