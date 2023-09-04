package com.syfuzzaman.swipeable_video_player_android

data class VideoList(
    var videos: List<VideoBean> = listOf()
)

data class VideoBean(
    var description: String? = null,
    var sources: String? = null,
    var subtitle: String? = null,
    var thumb: String? = null,
    var title: String? = null
)