package com.mehedi.filters_with_drawers_custom_payload.handlers

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterItem
import com.mehedi.filters_with_drawers_custom_payload.PriceRangeData

class PriceRangeFilterHandler(private val filterManager: FilterOperations) : FilterHandler {
    override fun handleFilter(filter: FilterItem) {
        val priceData = filter.data as PriceRangeData
        val filterContent = FilterContent(
            id = "price_range",
            title = "₹${priceData.minPrice} - ₹${priceData.maxPrice}",
            color = null,
            icon = null
        )

        val priceFilter = Filter(
            id = "price_filter",
            title = "Price Range",
            from = "ATTRIBUTE",
            type = "range",
            content = listOf(filterContent)
        )

        filterManager.updateFilter(priceFilter)
    }

    override fun clearSelection() {
        filterManager.removeFilterContent("price_filter", "price_range")
    }
} 