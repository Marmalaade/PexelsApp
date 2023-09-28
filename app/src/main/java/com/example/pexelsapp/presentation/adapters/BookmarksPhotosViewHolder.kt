package com.example.pexelsapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.BookmarksItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel

class BookmarksPhotosViewHolder(
    private val binding: BookmarksItemBinding,
    private val itemClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(savedPhoto: CuratedPhotoModel) {

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .timeout(AppConfig.getCachingTimeout())

        Glide.with(itemView.context)
            .load(savedPhoto.url)
            .apply(requestOptions)
            .into(binding.curatedPhotoItem)

        binding.authorsInitials.text = savedPhoto.photographer
        itemView.setOnClickListener {
            itemView.animate().scaleX(0.9f).scaleY(0.9f).setDuration(50).withEndAction {
                itemView.animate().scaleX(1f).scaleY(1f).setDuration(50).start()
            }.start()
            itemClick(savedPhoto.id)
        }

    }
}

