package com.mehedi.filters_with_drawers_custom_payload.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mehedi.filters_with_drawers_custom_payload.ColorData
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.databinding.ItemColorChipLayoutBinding


// Color Adapter implementation for reference
class ColorAdapter : ListAdapter<ColorData, ColorAdapter.ColorViewHolder>(ColorDiffCallback()) {
    private val selectedColors = mutableSetOf<ColorData>()
    private var onColorSelected: ((ColorData) -> Unit)? = null
    private var onColorDeselected: ((ColorData) -> Unit)? = null

    inner class ColorViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_color_chip, parent, false)
    ) {
        private val colorChip: Chip = itemView.findViewById(R.id.colorChip)

        fun bind(color: ColorData) {
            colorChip.apply {
                text = color.colorName
                isChecked = selectedColors.contains(color)

                chipBackgroundColor = ColorStateList.valueOf(
                    Color.parseColor(color.hexCode)
                )

                setTextColor(getContrastColor(color.hexCode))

                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedColors.add(color)
                        onColorSelected?.invoke(color)
                    } else {
                        selectedColors.remove(color)
                        onColorDeselected?.invoke(color)
                    }
                }
            }
        }

        private fun getContrastColor(hexColor: String): Int {
            val color = Color.parseColor(hexColor)
            val darkness = 1 - (0.299 * Color.red(color) +
                    0.587 * Color.green(color) +
                    0.114 * Color.blue(color)) / 255
            return if (darkness < 0.5) Color.BLACK else Color.WHITE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setSelectedColors(colors: List<ColorData>) {
        selectedColors.clear()
        selectedColors.addAll(colors)
        notifyDataSetChanged()
    }

    fun setOnColorSelectedListener(listener: (ColorData) -> Unit) {
        onColorSelected = listener
    }

    fun setOnColorDeselectedListener(listener: (ColorData) -> Unit) {
        onColorDeselected = listener
    }
}

// DiffCallback for optimizing RecyclerView updates
class ColorDiffCallback : DiffUtil.ItemCallback<ColorData>() {
    override fun areItemsTheSame(oldItem: ColorData, newItem: ColorData): Boolean {
        return oldItem.colorName == newItem.colorName
    }

    override fun areContentsTheSame(oldItem: ColorData, newItem: ColorData): Boolean {
        return oldItem == newItem
    }
}
