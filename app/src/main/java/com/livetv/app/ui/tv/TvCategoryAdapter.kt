package com.livetv.app.ui.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.livetv.app.databinding.ItemCategoryTvBinding

class TvCategoryAdapter(
    private val categories: List<String>,
    private val onCategorySelected: (Int) -> Unit
) : RecyclerView.Adapter<TvCategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    inner class CategoryViewHolder(private val binding: ItemCategoryTvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, position: Int, isSelected: Boolean) {
            binding.tvCategoryName.text = name
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos == RecyclerView.NO_ID.toInt()) return@setOnClickListener
                val prev = selectedPosition
                selectedPosition = pos
                notifyItemChanged(prev)
                notifyItemChanged(pos)
                onCategorySelected(pos)
            }
            binding.root.setOnFocusChangeListener { v, hasFocus ->
                v.elevation = if (hasFocus) 8f else 0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryTvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position, position == selectedPosition)
    }

    override fun getItemCount() = categories.size
}
