package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterManager
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.SizeData
import com.mehedi.filters_with_drawers_custom_payload.adapters.SizeAdapter

class SizeFilterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_size_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val sizeRecyclerView: RecyclerView = itemView.findViewById(R.id.sizeRecyclerView)
    private val sizeAdapter = SizeAdapter()

    init {
        sizeRecyclerView.apply {
            adapter = sizeAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            visibility=View.VISIBLE
        }

        sizeAdapter.setOnSizeSelectedListener { sizeData ->
            val filterContent = FilterContent(
                id = sizeData.sizeCode,
                title = sizeData.sizeName,
                color = null,
                icon = null
            )

            val sizeFilter = Filter(
                id = "size_filter",
                title = "Available Sizes",
                from = "ATTRIBUTE",
                type = "multi_select",
                content = listOf(filterContent)
            )

            FilterManager.updateFilter(sizeFilter)
        }

        sizeAdapter.setOnSizeDeselectedListener { sizeData ->
            FilterManager.removeFilterContent("size_filter", sizeData.sizeCode)
        }
    }

    fun bind(category: FilterCategory, selectedItems: List<Any>?) {
        // Get the first item which contains our size filter data
        val filterItem = category.items.firstOrNull()
        if (filterItem != null) {
            // Set the title from the FilterItem
            filterTitle.text = filterItem.title

            try {
                // Cast the data to List<SizeData>
                val sizes = filterItem.data as? List<SizeData>
                if (sizes != null) {
                    Log.d("SizeFilter", "Binding ${sizes.size} sizes")
                    sizeAdapter.submitList(sizes)

                    // Handle selected items if any
                    selectedItems?.let {
                        if (it.isNotEmpty() && it.first() is SizeData) {
                            sizeAdapter.setSelectedSizes(it as List<SizeData>)
                        }
                    }
                } else {
                    Log.e("SizeFilter", "Failed to cast data to List<SizeData>")
                }
            } catch (e: Exception) {
                Log.e("SizeFilter", "Error binding sizes", e)
            }
        }
    }

    fun clearSelection() {
        sizeAdapter.clearSelection()
    }
}