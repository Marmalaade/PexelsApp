package com.example.pexelsapp.presentation.adapters

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.BookmarksItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel

class BookmarksPhotosViewHolder(
    private val binding: BookmarksItemBinding,
    private val itemClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(savedPhoto: CuratedPhotoModel) {

        binding.shimmerViewContainer.startShimmer()
        val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean = false

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.shimmerViewContainer.apply {
                    stopShimmer()
                    setShimmer(null)
                }
                return false
            }
        }

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .timeout(AppConfig.getCachingTimeout())

        Glide.with(itemView.context)
            .load(savedPhoto.url)
            .listener(requestListener)
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

