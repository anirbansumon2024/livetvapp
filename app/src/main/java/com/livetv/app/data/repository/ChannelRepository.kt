package com.livetv.app.data.repository

import com.livetv.app.data.model.Channel
import com.livetv.app.data.model.ChannelCategory
import com.livetv.app.data.model.StreamType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelRepository @Inject constructor() {

    // Replace placeholder URLs with your actual DASH MPD / HLS stream URLs
    private fun getAllChannels(): List<Channel> = listOf(
        Channel(
            id = 1,
            name = "Demo Channel 1",
            streamUrl = "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
            logoUrl = "https://via.placeholder.com/150/FF6B6B/ffffff?text=CH1",
            category = "Entertainment",
            streamType = StreamType.DASH,
            description = "Sample DASH stream channel"
        ),
        Channel(
            id = 2,
            name = "Demo Channel 2",
            streamUrl = "https://storage.googleapis.com/wvmedia/cenc/h264/tears_of_steel/tears_of_steel.mpd",
            logoUrl = "https://via.placeholder.com/150/4ECDC4/ffffff?text=CH2",
            category = "Entertainment",
            streamType = StreamType.DASH,
            description = "Sample DASH stream channel"
        ),
        Channel(
            id = 3,
            name = "Sports Live",
            streamUrl = "https://your-sports-dash-url.mpd",  // TODO: replace with real URL
            logoUrl = "https://via.placeholder.com/150/45B7D1/ffffff?text=SP",
            category = "Sports",
            streamType = StreamType.DASH,
            description = "Live sports channel"
        ),
        Channel(
            id = 4,
            name = "News 24",
            streamUrl = "https://your-news-dash-url.mpd",   // TODO: replace with real URL
            logoUrl = "https://via.placeholder.com/150/96CEB4/ffffff?text=NW",
            category = "News",
            streamType = StreamType.DASH,
            description = "24/7 news channel"
        ),
        Channel(
            id = 5,
            name = "Kids Zone",
            streamUrl = "https://your-kids-dash-url.mpd",   // TODO: replace with real URL
            logoUrl = "https://via.placeholder.com/150/FFEAA7/333333?text=KZ",
            category = "Kids",
            streamType = StreamType.DASH,
            description = "Kids entertainment channel"
        ),
        Channel(
            id = 6,
            name = "Movies HD",
            streamUrl = "https://your-movie-dash-url.mpd",  // TODO: replace with real URL
            logoUrl = "https://via.placeholder.com/150/DDA0DD/ffffff?text=MV",
            category = "Movies",
            streamType = StreamType.DASH,
            description = "HD Movies channel"
        )
    )

    suspend fun getChannels(): Result<List<Channel>> = runCatching {
        getAllChannels()
    }

    suspend fun getChannelsByCategory(): Result<List<ChannelCategory>> = runCatching {
        val channels = getAllChannels()
        channels.groupBy { it.category }
            .entries
            .mapIndexed { index, entry ->
                ChannelCategory(index, entry.key, entry.value)
            }
    }

    suspend fun getChannelById(id: Int): Result<Channel?> = runCatching {
        getAllChannels().find { it.id == id }
    }
}
