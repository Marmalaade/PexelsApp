package com.example.pexelsapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.pexelsapp.R
import com.example.pexelsapp.databinding.PopularRequestItemBinding
import com.example.pexelsapp.domain.models.RequestModel

class PopularRequestViewHolder(
    private val binding: PopularRequestItemBinding,
    private val itemClick: (String, Int) -> Unit,
    private val updateSelectedItem: (Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(request: RequestModel, position: Int, isActive: Boolean) {
        binding.apply {
            popularRequestsItem.text = request.title
            if (isActive) {
                popularRequestsItem.setBackgroundResource(R.drawable.active_popular_request_background)
                popularRequestsItem.setTextColor(itemView.context.getColor(R.color.white_primary))
            } else {
                popularRequestsItem.setBackgroundResource(R.drawable.inactive_popular_request_background)
                popularRequestsItem.setTextColor(itemView.context.getColor(R.color.black_primary))
            }
        }
        itemView.setOnClickListener {
            updateSelectedItem(position)
            itemClick.invoke(request.title, position)
        }
    }
}
