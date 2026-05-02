package com.livetv.app.ui.mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.livetv.app.R
import com.livetv.app.data.model.Channel
import com.livetv.app.databinding.ItemChannelMobileBinding

class MobileChannelAdapter(
    private val onChannelClick: (Channel) -> Unit
) : ListAdapter<Channel, MobileChannelAdapter.ChannelViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(old: Channel, new: Channel) = old.id == new.id
        override fun areContentsTheSame(old: Channel, new: Channel) = old == new
    }

    inner class ChannelViewHolder(private val binding: ItemChannelMobileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(channel: Channel) {
            binding.tvChannelName.text = channel.name
            binding.tvCategory.text = channel.category
            binding.tvLive.visibility =
                if (channel.isLive) android.view.View.VISIBLE else android.view.View.GONE

            Glide.with(binding.ivLogo)
                .load(channel.logoUrl)
                .placeholder(R.drawable.ic_channel_placeholder)
                .error(R.drawable.ic_channel_placeholder)
                .into(binding.ivLogo)

            binding.root.setOnClickListener { onChannelClick(channel) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val binding = ItemChannelMobileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChannelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
