package com.mehedi.filters_with_drawers_custom_payload.handlers

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterItem

class RadioFilterHandler(private val filterManager: FilterOperations) : FilterHandler {
    override fun handleFilter(filter: FilterItem) {
        val options = filter.data as List<String>
        val filterContents = options.map { option ->
            FilterContent(
                id = option.lowercase().replace(" ", "_"),
                title = option,
                color = null,
                icon = null
            )
        }

        val radioFilter = Filter(
            id = filter.id,
            title = filter.title,
            from = "ATTRIBUTE",
            type = "single_select",
            content = filterContents
        )

        filterManager.updateFilter(radioFilter)
    }

    override fun clearSelection() {
        filterManager.removeFilterContent("radio_filter", "")
    }
} 