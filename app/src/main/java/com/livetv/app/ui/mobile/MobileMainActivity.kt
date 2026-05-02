package com.livetv.app.ui.mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.livetv.app.data.model.Channel
import com.livetv.app.databinding.ActivityMobileMainBinding
import com.livetv.app.player.PlayerActivity
import com.livetv.app.ui.common.ChannelViewModel
import com.livetv.app.ui.common.UiState
import com.livetv.app.data.model.ChannelCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MobileMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMobileMainBinding
    private val viewModel: ChannelViewModel by viewModels()
    private lateinit var channelAdapter: MobileChannelAdapter

    // Fix: hold categories reference so tab listener doesn't need to be re-added
    private var currentCategories: List<ChannelCategory> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupTabListener()   // Fix: register listener once here, not inside observe
        observeViewModel()

        // Fix: swipe refresh reloads categories (not just flat channel list)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadByCategory()
        }
    }

    private fun setupRecyclerView() {
        channelAdapter = MobileChannelAdapter { channel -> openPlayer(channel) }
        binding.rvChannels.apply {
            layoutManager = GridLayoutManager(this@MobileMainActivity, 2)
            adapter = channelAdapter
        }
    }

    // Fix: single tab listener referencing the stable currentCategories list
    private fun setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val category = currentCategories.getOrNull(tab.position)
                if (category != null) {
                    channelAdapter.submitList(category.channels)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is UiState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.tvError.visibility = android.view.View.GONE
                }
                is UiState.Success -> {
                    currentCategories = state.data

                    binding.tabLayout.removeAllTabs()
                    state.data.forEach { category ->
                        binding.tabLayout.addTab(
                            binding.tabLayout.newTab().setText(category.name)
                        )
                    }
                    // Show first category channels by default
                    state.data.firstOrNull()?.let {
                        channelAdapter.submitList(it.channels)
                    }
                }
                is UiState.Error -> {
                    binding.tvError.text = state.message
                    binding.tvError.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    private fun openPlayer(channel: Channel) {
        Intent(this, PlayerActivity::class.java).also {
            it.putExtra(PlayerActivity.EXTRA_STREAM_URL, channel.streamUrl)
            it.putExtra(PlayerActivity.EXTRA_STREAM_TYPE, channel.streamType.name)
            it.putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, channel.name)
            startActivity(it)
        }
    }
}
