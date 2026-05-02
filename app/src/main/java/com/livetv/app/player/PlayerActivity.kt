package com.livetv.app.player

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.livetv.app.data.model.StreamType
import com.livetv.app.databinding.ActivityPlayerBinding

@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    companion object {
        const val EXTRA_STREAM_URL = "extra_stream_url"
        const val EXTRA_STREAM_TYPE = "extra_stream_type"
        const val EXTRA_CHANNEL_NAME = "extra_channel_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Keep screen on while playing
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: return
        val streamTypeName = intent.getStringExtra(EXTRA_STREAM_TYPE) ?: "DASH"
        val channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""
        val streamType = StreamType.valueOf(streamTypeName)

        binding.tvChannelName.text = channelName
        initPlayer(streamUrl, streamType)
    }

    private fun initPlayer(url: String, streamType: StreamType) {
        player = ExoPlayer.Builder(this).build().also { exo ->
            binding.playerView.player = exo

            val dataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setConnectTimeoutMs(15_000)
                .setReadTimeoutMs(15_000)

            val mediaSource = when (streamType) {
                StreamType.DASH -> DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                StreamType.HLS -> HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                else -> ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            }

            exo.setMediaSource(mediaSource)
            exo.prepare()
            exo.playWhenReady = true

            exo.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    binding.tvError.text = "Stream error: ${error.message}"
                    binding.tvError.visibility = android.view.View.VISIBLE
                }
                override fun onPlaybackStateChanged(state: Int) {
                    binding.progressBar.visibility = if (state == Player.STATE_BUFFERING)
                        android.view.View.VISIBLE else android.view.View.GONE
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}