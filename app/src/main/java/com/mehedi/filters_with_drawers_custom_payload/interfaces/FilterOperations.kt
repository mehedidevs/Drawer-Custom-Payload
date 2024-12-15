import com.mehedi.filters_with_drawers_custom_payload.Filter

interface FilterOperations {
    fun updateFilter(filter: Filter)
    fun removeFilterContent(filterId: String, contentId: String)
    fun clearFilters()
    fun getActiveFilters(): List<Filter>
    fun addListener(listener: (Map<String, Filter>) -> Unit)
    fun removeListener(listener: (Map<String, Filter>) -> Unit)
} 