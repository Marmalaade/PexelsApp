package com.example.pexelsapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pexelsapp.databinding.CuratedPhotoItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel

class CuratedPhotosViewHolder(
    private val binding: CuratedPhotoItemBinding,
    private val itemClick: (Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(curatedPhoto: CuratedPhotoModel) {
        Glide
            .with(itemView.context)
            .load(curatedPhoto.url)
            .into(binding.curatedPhotoItem)

        itemView.setOnClickListener {
            itemClick.invoke(curatedPhoto.id)
        }
    }
}