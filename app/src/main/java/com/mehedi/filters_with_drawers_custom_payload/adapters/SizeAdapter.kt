package com.mehedi.filters_with_drawers_custom_payload.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.SizeData


class SizeAdapter : ListAdapter<SizeData, SizeAdapter.SizeViewHolder>(SizeDiffCallback()) {
    private val selectedSizes = mutableSetOf<SizeData>()
    private var onSizeSelected: ((SizeData) -> Unit)? = null
    private var onSizeDeselected: ((SizeData) -> Unit)? = null

    inner class SizeViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_size_chip, parent, false)
    ) {
        private val sizeChip: Chip = itemView.findViewById(R.id.sizeChip)

        fun bind(size: SizeData) {
            sizeChip.apply {
                text = size.sizeName
                isChecked = selectedSizes.contains(size)

                // Remove existing listener to avoid duplicates
                setOnCheckedChangeListener(null)

                // Set new listener
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedSizes.add(size)
                        onSizeSelected?.invoke(size)
                    } else {
                        selectedSizes.remove(size)
                        onSizeDeselected?.invoke(size)
                    }
                }

                // Make sure chip is visible
                visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun setSelectedSizes(sizes: List<SizeData>) {
        selectedSizes.clear()
        selectedSizes.addAll(sizes)
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedSizes.clear()
        notifyDataSetChanged()
    }

    fun setOnSizeSelectedListener(listener: (SizeData) -> Unit) {
        onSizeSelected = listener
    }

    fun setOnSizeDeselectedListener(listener: (SizeData) -> Unit) {
        onSizeDeselected = listener
    }
}

class SizeDiffCallback : DiffUtil.ItemCallback<SizeData>() {
    override fun areItemsTheSame(oldItem: SizeData, newItem: SizeData): Boolean {
        // Compare by unique identifier
        return oldItem.sizeCode == newItem.sizeCode
    }

    override fun areContentsTheSame(oldItem: SizeData, newItem: SizeData): Boolean {
        // Compare all properties
        return oldItem == newItem
    }
}