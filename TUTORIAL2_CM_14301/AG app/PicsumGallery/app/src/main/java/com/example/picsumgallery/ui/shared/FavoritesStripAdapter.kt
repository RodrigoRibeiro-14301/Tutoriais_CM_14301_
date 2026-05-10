package com.example.picsumgallery.ui.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picsumgallery.databinding.ItemFavoriteCircleBinding
import com.example.picsumgallery.domain.ImageItem

/**
 * Adapter for the horizontal favorites strip at the top of the gallery.
 */
class FavoritesStripAdapter(
    private val onFavoriteClick: (ImageItem) -> Unit
) : ListAdapter<ImageItem, FavoritesStripAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFavoriteCircleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageItem) {
            Glide.with(binding.ivFavoriteThumbnail.context)
                .load(item.downloadUrl)
                .circleCrop()
                .into(binding.ivFavoriteThumbnail)

            binding.root.setOnClickListener { onFavoriteClick(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem) = oldItem == newItem
    }
}
