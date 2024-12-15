package com.mehedi.filters_with_drawers_custom_payload.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.ColorData
import com.mehedi.filters_with_drawers_custom_payload.R

class ColorAdapter : ListAdapter<ColorData, ColorAdapter.ColorViewHolder>(ColorDiffCallback()) {

    private val selectedColors = mutableSetOf<ColorData>()
    private var onColorSelectedListener: ((ColorData) -> Unit)? = null
    private var onColorDeselectedListener: ((ColorData) -> Unit)? = null

    fun setOnColorSelectedListener(listener: (ColorData) -> Unit) {
        onColorSelectedListener = listener
    }

    fun setOnColorDeselectedListener(listener: (ColorData) -> Unit) {
        onColorDeselectedListener = listener
    }

    fun setSelectedColors(colors: List<ColorData>) {
        selectedColors.clear()
        selectedColors.addAll(colors)
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedColors.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = getItem(position)
        holder.bind(color, selectedColors.contains(color))
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorView: View = itemView.findViewById(R.id.colorView)
        private val colorName: TextView = itemView.findViewById(R.id.colorName)
        private val selectedIndicator: ImageView = itemView.findViewById(R.id.selectedIndicator)

        fun bind(color: ColorData, isSelected: Boolean) {
            colorView.setBackgroundColor(android.graphics.Color.parseColor(color.colorHex))
            colorName.text = color.colorName
            selectedIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                if (isSelected) {
                    selectedColors.remove(color)
                    onColorDeselectedListener?.invoke(color)
                } else {
                    selectedColors.add(color)
                    onColorSelectedListener?.invoke(color)
                }
                notifyItemChanged(adapterPosition)
            }
        }
    }

    private class ColorDiffCallback : DiffUtil.ItemCallback<ColorData>() {
        override fun areItemsTheSame(oldItem: ColorData, newItem: ColorData): Boolean {
            return oldItem.colorHex == newItem.colorHex
        }

        override fun areContentsTheSame(oldItem: ColorData, newItem: ColorData): Boolean {
            return oldItem == newItem
        }
    }
}
