package com.syfuzzaman.swipeable_video_player_android

import com.syfuzzaman.swipeable_video_player_android.common.BaseListItemCallback
import com.syfuzzaman.swipeable_video_player_android.common.MyBaseAdapter
import com.syfuzzaman.swipeable_video_player_android.data.ShortsAPIResponse


class ShortsDemoAdapter(
    cb: BaseListItemCallback<ShortsAPIResponse.ShortsBean>
): MyBaseAdapter<ShortsAPIResponse.ShortsBean>(cb){
    override fun getLayoutIdForPosition(position: Int): Int {
        return R.layout.item_movie
    }
}