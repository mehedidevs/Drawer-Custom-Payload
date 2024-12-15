import com.mehedi.filters_with_drawers_custom_payload.FilterItem

interface FilterHandler {
    fun handleFilter(filter: FilterItem)
    fun clearSelection()
} 