package com.mehedi.filters_with_drawers_custom_payload.handlers

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.ColorData
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterItem

class ColorFilterHandler(private val filterManager: FilterOperations) : FilterHandler {
    override fun handleFilter(filter: FilterItem) {
        val colorData = filter.data as List<ColorData>
        colorData.forEach { color ->
            val filterContent = FilterContent(
                id = color.colorName,
                title = color.colorName,
                color = color.colorHex,
                icon = null
            )

            val colorFilter = Filter(
                id = "color_filter",
                title = "Colors",
                from = "ATTRIBUTE",
                type = "single_select",
                content = listOf(filterContent)
            )

            filterManager.updateFilter(colorFilter)
        }
    }

    override fun clearSelection() {
        filterManager.removeFilterContent("color_filter", "")
    }
} 