package com.livetv.app.ui.tv

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.livetv.app.R
import com.livetv.app.data.Channel

class TvChannelCardPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        val channel = item as? Channel ?: return
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = channel.name
        cardView.contentText = channel.category
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

        Glide.with(cardView.context)
            .load(channel.logoUrl)
            .placeholder(R.drawable.ic_channel_placeholder)
            .error(R.drawable.ic_channel_placeholder)
            .into(cardView.mainImageView!!)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.badgeImage = null
        cardView.mainImage = null
    }
}
