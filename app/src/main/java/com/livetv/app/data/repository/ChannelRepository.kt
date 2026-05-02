package com.livetv.app.data

object ChannelRepository {

    // Replace these URLs with your actual DASH MPD / HLS stream URLs
    fun getAllChannels(): List<Channel> = listOf(
        Channel(
            id = 1,
            name = "Demo Channel 1",
            streamUrl = "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
            logoUrl = "https://via.placeholder.com/150/FF6B6B/ffffff?text=CH1",
            category = "Entertainment",
            description = "Sample DASH stream channel"
        ),
        Channel(
            id = 2,
            name = "Demo Channel 2",
            streamUrl = "https://storage.googleapis.com/wvmedia/cenc/h264/tears_of_steel/tears_of_steel.mpd",
            logoUrl = "https://via.placeholder.com/150/4ECDC4/ffffff?text=CH2",
            category = "Entertainment",
            description = "Sample DASH stream channel"
        ),
        Channel(
            id = 3,
            name = "Sports Live",
            streamUrl = "https://your-sports-dash-url.mpd",
            logoUrl = "https://via.placeholder.com/150/45B7D1/ffffff?text=SP",
            category = "Sports",
            description = "Live sports channel"
        ),
        Channel(
            id = 4,
            name = "News 24",
            streamUrl = "https://your-news-dash-url.mpd",
            logoUrl = "https://via.placeholder.com/150/96CEB4/ffffff?text=NW",
            category = "News",
            description = "24/7 news channel"
        ),
        Channel(
            id = 5,
            name = "Kids Zone",
            streamUrl = "https://your-kids-dash-url.mpd",
            logoUrl = "https://via.placeholder.com/150/FFEAA7/333333?text=KZ",
            category = "Kids",
            description = "Kids entertainment channel"
        ),
        Channel(
            id = 6,
            name = "Movies HD",
            streamUrl = "https://your-movie-dash-url.mpd",
            logoUrl = "https://via.placeholder.com/150/DDA0DD/ffffff?text=MV",
            category = "Movies",
            description = "HD Movies channel"
        )
    )

    fun getCategories(): List<Category> {
        val channels = getAllChannels()
        val grouped = channels.groupBy { it.category }
        return grouped.entries.mapIndexed { index, entry ->
            Category(index, entry.key, entry.value)
        }
    }

    fun getChannelById(id: Int): Channel? = getAllChannels().find { it.id == id }
}
