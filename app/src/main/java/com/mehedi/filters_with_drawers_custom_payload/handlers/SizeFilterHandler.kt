package com.mehedi.filters_with_drawers_custom_payload.handlers

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterItem
import com.mehedi.filters_with_drawers_custom_payload.SizeData

class SizeFilterHandler(private val filterManager: FilterOperations) : FilterHandler {
    override fun handleFilter(filter: FilterItem) {
        val sizeData = filter.data as List<SizeData>
        sizeData.forEach { size ->
            val filterContent = FilterContent(
                id = size.sizeCode,
                title = size.sizeName,
                color = null,
                icon = null
            )

            val sizeFilter = Filter(
                id = "size_filter",
                title = "Sizes",
                from = "ATTRIBUTE",
                type = "single_select",
                content = listOf(filterContent)
            )

            filterManager.updateFilter(sizeFilter)
        }
    }

    override fun clearSelection() {
        filterManager.removeFilterContent("size_filter", "")
    }
} 