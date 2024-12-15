package com.mehedi.filters_with_drawers_custom_payload.viewholders

import FilterOperations
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mehedi.filters_with_drawers_custom_payload.ColorData
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.adapters.ColorAdapter

class ColorFilterViewHolder(
    parent: ViewGroup,
    private val onTitleClick: (String) -> Unit,
    private val filterOperations: FilterOperations
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_color_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val colorRecyclerView: RecyclerView = itemView.findViewById(R.id.colorRecyclerView)
    private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
    private val colorAdapter = ColorAdapter()

    init {
        filterTitle.setOnClickListener {
            val category = filterTitle.tag as FilterCategory
            onTitleClick(category.id)
        }

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
                color = colorData.colorHex,
                icon = null
            )

            val colorFilter = Filter(
                id = "color_filter",
                title = "Available Colors",
                from = "ATTRIBUTE",
                type = "multi_select",
                content = listOf(filterContent)
            )

            filterOperations.updateFilter(colorFilter)
        }

        colorAdapter.setOnColorDeselectedListener { colorData ->
            filterOperations.removeFilterContent("color_filter", colorData.colorName)
        }
    }

    fun bind(category: FilterCategory, selectedItems: List<Any>?, isExpanded: Boolean) {
        filterTitle.tag = category
        filterTitle.text = category.items.first().title

        // Update expand/collapse icon
        expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expand_less
            else R.drawable.ic_expand_more
        )

        // Show/hide content based on expanded state
        colorRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            val colors = category.items.first().data as? List<ColorData>
            colors?.let {
                colorAdapter.submitList(it)
                selectedItems?.let { selected ->
                    if (selected.isNotEmpty() && selected.first() is ColorData) {
                        colorAdapter.setSelectedColors(selected as List<ColorData>)
                    }
                }
            }
        }
    }

    fun clearSelection() {
        colorAdapter.clearSelection()
    }
}

