package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

class SizeFilterViewHolder(
    parent: ViewGroup,
    private val onTitleClick: (String) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_size_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val sizeRecyclerView: RecyclerView = itemView.findViewById(R.id.sizeRecyclerView)
    private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
    private val sizeAdapter = SizeAdapter()

    init {
        filterTitle.setOnClickListener {
            val category = filterTitle.tag as FilterCategory
            onTitleClick(category.id)
        }

        sizeRecyclerView.apply {
            adapter = sizeAdapter
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
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

    fun bind(category: FilterCategory, selectedItems: List<Any>?, isExpanded: Boolean) {
        filterTitle.tag = category
        filterTitle.text = category.items.first().title

        // Update expand/collapse icon
        expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expand_less 
            else R.drawable.ic_expand_more
        )
        
        // Show/hide content based on expanded state
        sizeRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            try {
                val sizes = category.items.first().data as? List<SizeData>
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