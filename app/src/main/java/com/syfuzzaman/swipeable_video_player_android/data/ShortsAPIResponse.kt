package com.syfuzzaman.swipeable_video_player_android.data

import com.google.gson.annotations.SerializedName

data class ShortsAPIResponse (
    @SerializedName("success" ) var success : Boolean?          = null,
    @SerializedName("shorts"  ) var shorts  : List<ShortsBean> = listOf()
) {
    data class Reactions (
        @SerializedName("likes"    ) var likes    : Int? = null,
        @SerializedName("dislikes" ) var dislikes : Int? = null
    )

    data class ShortsBean (
        @SerializedName("description"     ) var description     : String?    = null,
        @SerializedName("thumbnail"       ) var thumbnail       : String?    = null,
        @SerializedName("mediaUrl"        ) var mediaUrl        : String?    = null,
        @SerializedName("tags"            ) var tags            : String?    = null,
        @SerializedName("reactions"       ) var reactions       : Reactions? = Reactions(),
        @SerializedName("channelName"     ) var channelName     : String?    = null,
        @SerializedName("channelPhotoUrl" ) var channelPhotoUrl : String?    = null,
        @SerializedName("shareUrl"        ) var shareUrl        : String?    = null,
        @SerializedName("channelUrl"      ) var channelUrl      : String?    = null
    )
}