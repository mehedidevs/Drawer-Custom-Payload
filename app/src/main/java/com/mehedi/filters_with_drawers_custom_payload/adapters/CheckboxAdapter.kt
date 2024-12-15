package com.mehedi.filters_with_drawers_custom_payload.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.R

class CheckboxAdapter : ListAdapter<String, CheckboxAdapter.CheckboxViewHolder>(CheckboxDiffCallback()) {

    private val selectedItems = mutableSetOf<String>()
    private var onCheckboxSelectedListener: ((String) -> Unit)? = null
    private var onCheckboxDeselectedListener: ((String) -> Unit)? = null

    fun setOnCheckboxSelectedListener(listener: (String) -> Unit) {
        onCheckboxSelectedListener = listener
    }

    fun setOnCheckboxDeselectedListener(listener: (String) -> Unit) {
        onCheckboxDeselectedListener = listener
    }

    fun setSelectedItems(items: List<String>) {
        selectedItems.clear()
        selectedItems.addAll(items)
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return CheckboxViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckboxViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, selectedItems.contains(item))
    }

    inner class CheckboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(item: String, isSelected: Boolean) {
            checkbox.text = item
            checkbox.isChecked = isSelected

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(item)
                    onCheckboxSelectedListener?.invoke(item)
                } else {
                    selectedItems.remove(item)
                    onCheckboxDeselectedListener?.invoke(item)
                }
            }
        }
    }

    private class CheckboxDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}