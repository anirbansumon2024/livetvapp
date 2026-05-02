package com.livetv.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class StreamType {
    DASH, HLS, PROGRESSIVE
}

@Parcelize
data class Channel(
    val id: Int,
    val name: String,
    val streamUrl: String,
    val logoUrl: String,
    val category: String,
    val streamType: StreamType = StreamType.DASH,
    val isLive: Boolean = true,
    val description: String = ""
) : Parcelable

@Parcelize
data class ChannelCategory(
    val id: Int,
    val name: String,
    val channels: List<Channel>
) : Parcelable
