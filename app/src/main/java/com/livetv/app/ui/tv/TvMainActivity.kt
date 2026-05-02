package com.livetv.app.ui.tv

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.livetv.app.data.model.Channel
import com.livetv.app.data.model.ChannelCategory
import com.livetv.app.databinding.ActivityTvMainBinding
import com.livetv.app.player.PlayerActivity
import com.livetv.app.ui.common.ChannelViewModel
import com.livetv.app.ui.common.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvMainActivity : FragmentActivity() {

    private lateinit var binding: ActivityTvMainBinding
    private val viewModel: ChannelViewModel by viewModels()
    private lateinit var tvChannelAdapter: TvChannelAdapter

    private var currentCategories: List<ChannelCategory> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGrid()
        observeViewModel()
        viewModel.loadByCategory()
    }

    private fun setupGrid() {
        tvChannelAdapter = TvChannelAdapter { channel -> openPlayer(channel) }
        binding.rvTvChannels.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                this@TvMainActivity, 4
            )
            adapter = tvChannelAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                    binding.tvError.visibility = android.view.View.GONE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    currentCategories = state.data

                    val categoryAdapter = TvCategoryAdapter(state.data.map { it.name }) { index ->
                        currentCategories.getOrNull(index)?.let { category ->
                            tvChannelAdapter.submitList(category.channels)
                            binding.tvCurrentCategory.text = category.name
                        }
                    }
                    binding.rvCategories.adapter = categoryAdapter

                    state.data.firstOrNull()?.let {
                        tvChannelAdapter.submitList(it.channels)
                        binding.tvCurrentCategory.text = it.name
                    }
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.tvError.text = state.message
                    binding.tvError.visibility = android.view.View.VISIBLE
                }
            }
        }
    }

    fun openPlayer(channel: Channel) {
        Intent(this, PlayerActivity::class.java).also {
            it.putExtra(PlayerActivity.EXTRA_STREAM_URL, channel.streamUrl)
            it.putExtra(PlayerActivity.EXTRA_STREAM_TYPE, channel.streamType.name)  // Fix: streamType now exists
            it.putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, channel.name)
            startActivity(it)
        }
    }
}
