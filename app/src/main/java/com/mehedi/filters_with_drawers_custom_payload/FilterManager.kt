package com.mehedi.filters_with_drawers_custom_payload

import FilterOperations
import android.util.Log

object FilterManager : FilterOperations {
    private val filters = mutableMapOf<String, Filter>()
    private val listeners = mutableListOf<(Map<String, Filter>) -> Unit>()

    override fun updateFilter(filter: Filter) {
        if (filter.type == "single_select") {
            filters[filter.id] = filter
        } else {
            val existingFilter = filters[filter.id]
            if (existingFilter != null) {
                val mergedContent = (existingFilter.content + filter.content).distinctBy { it.id }
                filters[filter.id] = existingFilter.copy(content = mergedContent)
            } else {
                filters[filter.id] = filter
            }
        }
        notifyListeners()
    }

    override fun removeFilterContent(filterId: String, contentId: String) {
        filters[filterId]?.let { filter ->
            val updatedContent = filter.content.filterNot { it.id == contentId }
            if (updatedContent.isEmpty()) {
                filters.remove(filterId)
            } else {
                filters[filterId] = filter.copy(content = updatedContent)
            }
            notifyListeners()
        }
    }

    override fun clearFilters() {
        filters.clear()
        notifyListeners()
    }

    override fun getActiveFilters(): List<Filter> = filters.values.toList()

    override fun addListener(listener: (Map<String, Filter>) -> Unit) {
        listeners.add(listener)
        listener(filters)
    }

    override fun removeListener(listener: (Map<String, Filter>) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it(filters) }
    }

    fun logCurrentState() {
        Log.d("FilterManager", "Current filters: ${filters.size}")
        filters.forEach { (id, filter) ->
            Log.d("FilterManager", "Filter: $id")
            Log.d("FilterManager", "Type: ${filter.type}")
            Log.d("FilterManager", "Content items: ${filter.content.size}")
            filter.content.forEach { content ->
                Log.d("FilterManager", "  - ${content.title}")
            }
        }
    }
}
// Data classes remain the same
data class FilterPayload(
    var keyword: String = "",
    var categorySlug: String = "",
    var filters: MutableList<Filter> = mutableListOf(),
    var sortBy: String = ""
)

data class Filter(
    val id: String,
    val title: String,
    val from: String,
    val type: String,
    val content: List<FilterContent>
)

