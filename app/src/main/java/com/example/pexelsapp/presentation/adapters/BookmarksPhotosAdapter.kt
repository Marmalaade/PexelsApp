package com.example.pexelsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.BookmarksItemBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.generics.DiffCallback
import com.example.pexelsapp.presentation.viewholders.BookmarksPhotosViewHolder

class BookmarksPhotosAdapter : RecyclerView.Adapter<BookmarksPhotosViewHolder>() {
    var itemClick: (Int) -> Unit = {}

    private val savedPhotos = mutableListOf<CuratedPhotoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksPhotosViewHolder {
        val binding =
            BookmarksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarksPhotosViewHolder(binding, itemClick)
    }

    override fun getItemCount() = savedPhotos.size

    override fun onBindViewHolder(holder: BookmarksPhotosViewHolder, position: Int) {
        holder.onBind(savedPhotos[position])
    }

    fun updateSavedPhotos(newList: List<CuratedPhotoModel>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                oldList = savedPhotos,
                newList = newList,
                areItemsTheSame = { old, new -> old.id == new.id },
                areContentsTheSame = { old, new -> old == new }
            )
        )
        savedPhotos.clear()
        savedPhotos.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}