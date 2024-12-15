package com.mehedi.filters_with_drawers_custom_payload

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.viewholders.CheckboxFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.ColorFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.PriceRangeFilterViewHolder
import com.mehedi.filters_with_drawers_custom_payload.viewholders.SizeFilterViewHolder

// Adapter for the main filter RecyclerView
class FilterAdapter(
    private val categories: List<FilterCategory>,
    private val onFilterChanged: (FilterItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val selectedItems = mutableMapOf<String, List<Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FilterType.COLOR.ordinal -> ColorFilterViewHolder(parent)
            FilterType.SIZE.ordinal -> SizeFilterViewHolder(parent)
            FilterType.PRICE_RANGE.ordinal -> PriceRangeFilterViewHolder(parent)
            else -> CheckboxFilterViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = categories[position]
        when (holder) {
            is ColorFilterViewHolder -> holder.bind(category, selectedItems[category.id])
            is SizeFilterViewHolder -> holder.bind(category, selectedItems[category.id])
            is PriceRangeFilterViewHolder -> holder.bind(category)
            is CheckboxFilterViewHolder -> holder.bind(category, selectedItems[category.id])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return categories[position].items.first().type.ordinal
    }

    override fun getItemCount(): Int = categories.size

    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }
}