package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mehedi.filters_with_drawers_custom_payload.ColorData
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterManager
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.adapters.ColorAdapter

class ColorFilterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_color_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val colorRecyclerView: RecyclerView = itemView.findViewById(R.id.colorRecyclerView)
    private val colorAdapter = ColorAdapter()

    init {
        colorRecyclerView.apply {
            adapter = colorAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }

        colorAdapter.setOnColorSelectedListener { colorData ->
            val filterContent = FilterContent(
                id = colorData.colorName,
                title = colorData.colorName,
                color = colorData.hexCode,
                icon = null
            )

            val colorFilter = Filter(
                id = "color_filter",
                title = "Colors",
                from = "ATTRIBUTE",
                type = "multi_select",
                content = listOf(filterContent)
            )

            FilterManager.updateFilter(colorFilter)
        }

        colorAdapter.setOnColorDeselectedListener { colorData ->
            FilterManager.removeFilterContent("color_filter", colorData.colorName)
        }
    }

    fun bind(category: FilterCategory, selectedColors: List<Any>?) {
        filterTitle.text = category.name

        val colors = (category.items.first().data as List<ColorData>)
        colorAdapter.submitList(colors)
        selectedColors?.let {
            colorAdapter.setSelectedColors(it as List<ColorData>)
        }
    }
}

