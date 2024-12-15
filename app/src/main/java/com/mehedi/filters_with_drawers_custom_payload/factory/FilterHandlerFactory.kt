package com.mehedi.filters_with_drawers_custom_payload.factory

import FilterHandler
import FilterOperations
import com.mehedi.filters_with_drawers_custom_payload.FilterType
import com.mehedi.filters_with_drawers_custom_payload.handlers.CheckboxFilterHandler
import com.mehedi.filters_with_drawers_custom_payload.handlers.ColorFilterHandler
import com.mehedi.filters_with_drawers_custom_payload.handlers.PriceRangeFilterHandler
import com.mehedi.filters_with_drawers_custom_payload.handlers.RadioFilterHandler
import com.mehedi.filters_with_drawers_custom_payload.handlers.SizeFilterHandler

class FilterHandlerFactory(private val filterManager: FilterOperations) {
    fun createHandler(type: FilterType): FilterHandler {
        return when (type) {
            FilterType.COLOR -> ColorFilterHandler(filterManager)
            FilterType.SIZE -> SizeFilterHandler(filterManager)
            FilterType.PRICE_RANGE -> PriceRangeFilterHandler(filterManager)
            FilterType.CHECKBOX -> CheckboxFilterHandler(filterManager)
            FilterType.RADIO -> RadioFilterHandler(filterManager)
        }
    }
} 