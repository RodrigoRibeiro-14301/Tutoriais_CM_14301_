package com.example.picsumgallery.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picsumgallery.R
import com.example.picsumgallery.databinding.ItemImageCardBinding
import com.example.picsumgallery.domain.ImageItem

/**
 * Adapter for the main gallery grid.
 */
open class GalleryAdapter(
    private val onItemClick: (ImageItem) -> Unit,
    private val onFavoriteClick: (ImageItem) -> Unit
) : ListAdapter<ImageItem, GalleryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemImageCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageItem) {
            binding.tvAuthor.text = item.author
            
            // Load image using Glide
            Glide.with(binding.ivThumbnail.context)
                .load(item.downloadUrl)
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into(binding.ivThumbnail)

            // Update favorite icon
            val favoriteIcon = if (item.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_border
            val favoriteTint = if (item.isFavorite) R.color.heart_active else R.color.heart_inactive
            
            binding.ivFavorite.setImageResource(favoriteIcon)
            binding.ivFavorite.setColorFilter(binding.root.context.getColor(favoriteTint))

            // Listeners
            binding.root.setOnClickListener { onItemClick(item) }
            binding.ivFavorite.setOnClickListener { onFavoriteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem) = oldItem == newItem
    }
}
