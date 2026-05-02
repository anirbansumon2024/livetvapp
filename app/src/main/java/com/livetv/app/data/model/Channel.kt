package com.livetv.app.data

import java.io.Serializable

data class Channel(
    val id: Int,
    val name: String,
    val streamUrl: String,       // DASH MPD or HLS URL
    val logoUrl: String,
    val category: String,
    val isLive: Boolean = true,
    val description: String = ""
) : Serializable

data class Category(
    val id: Int,
    val name: String,
    val channels: List<Channel>
)
