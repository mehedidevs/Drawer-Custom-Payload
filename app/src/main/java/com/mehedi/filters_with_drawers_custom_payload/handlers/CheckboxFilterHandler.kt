package com.mehedi.filters_with_drawers_custom_payload.handlers

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterItem

class CheckboxFilterHandler(private val filterManager: FilterOperations) : FilterHandler {
    override fun handleFilter(filter: FilterItem) {
        val categories = filter.data as List<String>
        val filterContents = categories.map { category ->
            FilterContent(
                id = category.lowercase().replace(" ", "_"),
                title = category,
                color = null,
                icon = null
            )
        }

        val categoryFilter = Filter(
            id = "category_filter",
            title = "Categories",
            from = "ATTRIBUTE",
            type = "multi_select",
            content = filterContents
        )

        filterManager.updateFilter(categoryFilter)
    }

    override fun clearSelection() {
        filterManager.removeFilterContent("category_filter", "")
    }
} 