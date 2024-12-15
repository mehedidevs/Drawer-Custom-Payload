package com.mehedi.filters_with_drawers_custom_payload

// Data classes for filter types
data class FilterCategory(
    val id: String,
    val name: String,
    val items: List<FilterItem>
)

data class FilterItem(
    val id: String,
    val title: String,
    val type: FilterType,
    val data: Any
)

enum class FilterType {
    COLOR,
    SIZE,
    PRICE_RANGE,
    CHECKBOX,
    RADIO
}

data class ColorData(
    val colorName: String,
    val hexCode: String
)

data class SizeData(
    val sizeName: String,
    val sizeCode: String
)

data class PriceRangeData(
    var minPrice: Float,
    var maxPrice: Float
)