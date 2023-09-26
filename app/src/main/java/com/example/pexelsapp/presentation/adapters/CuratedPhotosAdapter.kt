package com.example.pexelsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.CuratedPhotoItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.generics.GenericDiffCallback

class CuratedPhotosAdapter : RecyclerView.Adapter<CuratedPhotosViewHolder>() {
    var itemClick: ((Int) -> Unit) = {}

    private val curatedPhotos = mutableListOf<CuratedPhotoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuratedPhotosViewHolder {
        val binding =
            CuratedPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CuratedPhotosViewHolder(binding, itemClick)
    }

    override fun getItemCount() = curatedPhotos.size

    override fun onBindViewHolder(holder: CuratedPhotosViewHolder, position: Int) {
        holder.onBind(curatedPhotos[position])
    }

    fun updateCuratedPhotos(newList: List<CuratedPhotoModel>) {
        val diffResult = DiffUtil.calculateDiff(
            GenericDiffCallback(
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