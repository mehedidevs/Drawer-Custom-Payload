package com.mehedi.filters_with_drawers_custom_payload

data class FilterContent(
    val id: String,
    val title: String,
    val color: String? = null,
    val icon: String? = null
) 