package com.example.pexelsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.CuratedPhotoItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.generics.DiffCallback

class CuratedPhotosAdapter : RecyclerView.Adapter<CuratedPhotosViewHolder>() {
    var itemClick: ((Int) -> Unit) = {}

    private val curatedPhotos = mutableListOf<CuratedPhotoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuratedPhotosViewHolder {
        val binding =
            CuratedPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = CuratedPhotosViewHolder(binding, itemClick)
        viewHolder.itemView.animation = AnimationUtils.loadAnimation(viewHolder.itemView.context, R.anim.appear_photos_list_animation)
        return viewHolder
    }

    override fun getItemCount() = curatedPhotos.size

    override fun onBindViewHolder(holder: CuratedPhotosViewHolder, position: Int) {
        holder.onBind(curatedPhotos[position])
    }

    fun updateCuratedPhotos(newList: List<CuratedPhotoModel>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                oldList = curatedPhotos,
                newList = newList,
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )
        curatedPhotos.clear()
        curatedPhotos.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}