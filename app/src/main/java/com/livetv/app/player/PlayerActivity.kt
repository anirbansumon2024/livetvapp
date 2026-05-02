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
import android.view.View

@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null
    private var wasPlaying = true  // Fix: track user intent to avoid auto-resume after manual pause

    companion object {
        const val EXTRA_STREAM_URL = "extra_stream_url"
        const val EXTRA_STREAM_TYPE = "extra_stream_type"
        const val EXTRA_CHANNEL_NAME = "extra_channel_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: run {
            finish()
            return
        }
        val streamTypeName = intent.getStringExtra(EXTRA_STREAM_TYPE) ?: StreamType.DASH.name
        val channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""

        val streamType = runCatching { StreamType.valueOf(streamTypeName) }
            .getOrDefault(StreamType.DASH)

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
                StreamType.PROGRESSIVE -> ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            }

            exo.setMediaSource(mediaSource)
            exo.prepare()
            exo.playWhenReady = true

            exo.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    binding.tvError.text = "Stream error: ${error.message}"
                    binding.tvError.visibility = View.VISIBLE
                }

                override fun onPlaybackStateChanged(state: Int) {
                    binding.progressBar.visibility =
                        if (state == Player.STATE_BUFFERING) View.VISIBLE else View.GONE
                }

                // Fix: track actual playing state so onResume respects user intent
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (exo.playbackState != Player.STATE_BUFFERING) {
                        wasPlaying = isPlaying
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        wasPlaying = player?.isPlaying ?: false
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        // Fix: only resume if user was actually playing before pause
        if (wasPlaying) {
            player?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
