package com.mehedi.filters_with_drawers_custom_payload

import android.util.Log

object FilterManager {
    private val filters = mutableMapOf<String, Filter>()
    private val listeners = mutableListOf<(Map<String, Filter>) -> Unit>()

    // Update or add a new filter
    fun updateFilter(filter: Filter) {
        // For single select filters, we keep only one content item
        if (filter.type == "single_select") {
            filters[filter.id] = filter
        } else {
            // For multi-select, we merge the content with existing filter
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

    // Remove a specific content item from a filter
    fun removeFilterContent(filterId: String, contentId: String) {
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

    fun clearFilters() {
        filters.clear()
        notifyListeners()
    }

    fun getActiveFilters(): List<Filter> = filters.values.toList()

    // Add a listener
    fun addListener(listener: (Map<String, Filter>) -> Unit) {
        listeners.add(listener)
        // Notify immediately of current state
        listener(filters)
    }

    // Remove a listener
    fun removeListener(listener: (Map<String, Filter>) -> Unit) {
        listeners.remove(listener)
    }

    // Notify all listeners of changes
    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener(filters)
        }
    }

    // Debug method to log current state
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

data class FilterContent(
    val id: String,
    val title: String,
    val color: String?,
    val icon: String?
)