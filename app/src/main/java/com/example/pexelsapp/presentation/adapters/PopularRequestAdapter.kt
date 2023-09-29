package com.example.pexelsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.databinding.PopularRequestItemBinding
import com.example.pexelsapp.domain.models.RequestModel
import com.example.pexelsapp.presentation.generics.DiffCallback

class PopularRequestAdapter : RecyclerView.Adapter<PopularRequestViewHolder>() {

    var itemClick: ((String, Int) -> Unit) = { _, _ -> }

    private val requests = mutableListOf<RequestModel>()
    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularRequestViewHolder {
        val binding = PopularRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularRequestViewHolder(binding, itemClick) { newPosition ->
            updateSelectedItemPosition(newPosition)
        }
    }

    override fun onBindViewHolder(holder: PopularRequestViewHolder, position: Int) {
        holder.onBind(requests[position], position, position == selectedItemPosition)
    }

    override fun getItemCount() = requests.size

    fun updateRequests(newList: List<RequestModel>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffCallback(
                oldList = requests,
                newList = newList,
                areItemsTheSame = { old, new -> old.title == new.title },
                areContentsTheSame = { old, new -> old == new }
            )
        )
        requests.clear()
        requests.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSelectedItemPosition(position: Int) {
        val prev = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(prev)
        notifyItemChanged(position)
    }
}




