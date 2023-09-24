package com.nexdecade.nd_shorts.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ShortsAPIResponse (
    @SerializedName("success" ) var success : Boolean?          = null,
    @SerializedName("shorts"  ) var shorts  : List<ShortsBean> = listOf()
)

data class Reactions (
    @SerializedName("likes"    ) var likes    : Int? = null,
    @SerializedName("dislikes" ) var dislikes : Int? = null
)

@Parcelize
data class ShortsBean (
    @SerializedName("id"              ) var id              : Int?       = null,
    @SerializedName("description"     ) var description     : String?    = null,
    @SerializedName("thumbnail"       ) var thumbnail       : String?    = null,
    @SerializedName("mediaUrl"        ) var mediaUrl        : String?    = null,
    @SerializedName("tags"            ) var tags            : String?    = null,
//    @SerializedName("reactions"       ) var reactions       : @RawValue Reactions? = null,
    @SerializedName("channelName"     ) var channelName     : String?    = null,
    @SerializedName("channelPhotoUrl" ) var channelPhotoUrl : String?    = null,
    @SerializedName("shareUrl"        ) var shareUrl        : String?    = null,
    @SerializedName("channelUrl"      ) var channelUrl      : String?    = null
): Parcelable