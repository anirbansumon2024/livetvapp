package com.livetv.app.ui.tv

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.livetv.app.R
import com.livetv.app.data.Channel
import com.livetv.app.data.ChannelRepository

class TvBrowseFragment : BrowseSupportFragment() {

    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadChannels()
    }

    private fun setupUI() {
        title = getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireContext(), R.color.tv_brand_color)
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.tv_search_color)

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        adapter = rowsAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is Channel) {
                (activity as? TvMainActivity)?.openPlayer(item)
            }
        }

        setOnSearchClickedListener {
            // TODO: Implement TV Search
        }
    }

    private fun loadChannels() {
        val categories = ChannelRepository.getCategories()
        rowsAdapter.clear()

        // Add "All Channels" row first
        val allChannels = ChannelRepository.getAllChannels()
        addChannelRow("🔴 Live Now", allChannels)

        // Add category rows
        categories.forEach { category ->
            addChannelRow(category.name, category.channels)
        }
    }

    private fun addChannelRow(title: String, channels: List<Channel>) {
        val cardPresenter = TvChannelCardPresenter()
        val listRowAdapter = ArrayObjectAdapter(cardPresenter)

        channels.forEach { channel ->
            listRowAdapter.add(channel)
        }

        val header = HeaderItem(title)
        rowsAdapter.add(ListRow(header, listRowAdapter))
    }
}
