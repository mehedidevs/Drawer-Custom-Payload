package com.mehedi.filters_with_drawers_custom_payload

import FilterOperations
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.factory.FilterHandlerFactory
import com.mehedi.filters_with_drawers_custom_payload.viewholders.CheckboxFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.ColorFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.PriceRangeFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.SizeFilterViewHolder

// Adapter for the main filter RecyclerView
class FilterAdapter(
    private val categories: List<FilterCategory>,
    private val onFilterChanged: (FilterItem) -> Unit,
    private val filterHandlerFactory: FilterHandlerFactory,
    private val filterOperations: FilterOperations
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val selectedItems = mutableMapOf<String, List<Any>>()
    private val expandedCategories = mutableSetOf<String>() // Track expanded categories

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FilterType.COLOR.ordinal -> ColorFilterViewHolder(
                parent = parent,
                onTitleClick = { categoryId -> toggleCategory(categoryId) },
                filterOperations = filterOperations
            )
            FilterType.SIZE.ordinal -> SizeFilterViewHolder(parent, ::toggleCategory)
            FilterType.PRICE_RANGE.ordinal -> PriceRangeFilterViewHolder(parent, ::toggleCategory)
            else -> CheckboxFilterViewHolder(
                parent = parent,
                onTitleClick = ::toggleCategory,
                filterOperations = filterOperations
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = categories[position]
        val isExpanded = expandedCategories.contains(category.id)
        
        when (holder) {
            is ColorFilterViewHolder -> holder.bind(category, selectedItems[category.id], isExpanded)
            is SizeFilterViewHolder -> holder.bind(category, selectedItems[category.id], isExpanded)
            is PriceRangeFilterViewHolder -> holder.bind(category, isExpanded)
            is CheckboxFilterViewHolder -> holder.bind(category, selectedItems[category.id], isExpanded)
        }
    }

    private fun toggleCategory(categoryId: String) {
        if (expandedCategories.contains(categoryId)) {
            expandedCategories.remove(categoryId)
        } else {
            expandedCategories.add(categoryId)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return categories[position].items.first().type.ordinal
    }

    override fun getItemCount(): Int = categories.size

    fun clearSelections() {
        categories.forEach { category ->
            category.items.forEach { item ->
                filterHandlerFactory.createHandler(item.type).clearSelection()
            }
        }
        notifyDataSetChanged()
    }
}