package com.mehedi.filters_with_drawers_custom_payload.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.databinding.ItemCheckboxBinding

// Checkbox Adapter implementation for reference
class CheckboxAdapter : ListAdapter<String, CheckboxAdapter.CheckboxViewHolder>(CheckboxDiffCallback()) {
    private val selectedItems = mutableSetOf<String>()
    private var onItemSelected: ((String) -> Unit)? = null
    private var onItemDeselected: ((String) -> Unit)? = null

    inner class CheckboxViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox, parent, false)
    ) {
        private val checkbox: MaterialCheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(item: String) {
            checkbox.apply {
                text = item
                isChecked = selectedItems.contains(item)
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedItems.add(item)
                        onItemSelected?.invoke(item)
                    } else {
                        selectedItems.remove(item)
                        onItemDeselected?.invoke(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxViewHolder {
        return CheckboxViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CheckboxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setSelectedItems(items: List<String>) {
        selectedItems.clear()
        selectedItems.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnCheckboxSelectedListener(listener: (String) -> Unit) {
        onItemSelected = listener
    }

    fun setOnCheckboxDeselectedListener(listener: (String) -> Unit) {
        onItemDeselected = listener
    }
}

class CheckboxDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        // Compare strings directly as they serve as unique identifiers
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        // Strings are immutable, so if they're the same reference, their content is the same
        return oldItem == newItem
    }
}