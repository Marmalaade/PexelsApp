package com.example.pexelsapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.CuratedPhotoItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel

class CuratedPhotosViewHolder(
    private val binding: CuratedPhotoItemBinding,
    private val itemClick: (Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(curatedPhoto: CuratedPhotoModel) {

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .timeout(AppConfig.getCachingTimeout())

        Glide
            .with(itemView.context)
            .load(curatedPhoto.url)
            .apply(requestOptions)
            .into(binding.curatedPhotoItem)

        itemView.setOnClickListener {
            itemClick.invoke(curatedPhoto.id)
        }
    }
}