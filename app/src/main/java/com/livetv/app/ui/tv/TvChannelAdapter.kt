package com.livetv.app.ui.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livetv.app.R
import com.livetv.app.data.model.Channel
import com.livetv.app.databinding.ItemChannelTvBinding

class TvChannelAdapter(
    private val onChannelClick: (Channel) -> Unit
) : ListAdapter<Channel, TvChannelAdapter.TvChannelViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(old: Channel, new: Channel) = old.id == new.id
        override fun areContentsTheSame(old: Channel, new: Channel) = old == new
    }

    inner class TvChannelViewHolder(private val binding: ItemChannelTvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(channel: Channel) {
            binding.tvChannelName.text = channel.name
            binding.tvLive.visibility =
                if (channel.isLive) android.view.View.VISIBLE else android.view.View.GONE

            Glide.with(binding.ivLogo)
                .load(channel.logoUrl)
                .placeholder(R.drawable.ic_channel_placeholder)
                .error(R.drawable.ic_channel_placeholder)
                .into(binding.ivLogo)

            binding.root.setOnClickListener { onChannelClick(channel) }
            binding.root.setOnFocusChangeListener { v, hasFocus ->
                v.scaleX = if (hasFocus) 1.1f else 1.0f
                v.scaleY = if (hasFocus) 1.1f else 1.0f
                v.elevation = if (hasFocus) 12f else 2f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvChannelViewHolder {
        val binding = ItemChannelTvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TvChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
