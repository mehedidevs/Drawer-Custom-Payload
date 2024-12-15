package com.mehedi.filters_with_drawers_custom_payload.viewholders

import FilterOperations
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.adapters.CheckboxAdapter

class CheckboxFilterViewHolder(
    parent: ViewGroup,
    private val onTitleClick: (String) -> Unit,
    private val filterOperations: FilterOperations
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val checkboxRecyclerView: RecyclerView = itemView.findViewById(R.id.checkboxRecyclerView)
    private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
    private val checkboxAdapter = CheckboxAdapter()

    init {
        filterTitle.setOnClickListener {
            val category = filterTitle.tag as FilterCategory
            onTitleClick(category.id)
        }

        checkboxRecyclerView.apply {
            adapter = checkboxAdapter
            layoutManager = LinearLayoutManager(context)
        }

        checkboxAdapter.setOnCheckboxSelectedListener { item ->
            val filterContent = FilterContent(
                id = item.lowercase().replace(" ", "_"),
                title = item,
                color = null,
                icon = null
            )

            val checkboxFilter = Filter(
                id = "checkbox_filter",
                title = filterTitle.text.toString(),
                from = "ATTRIBUTE",
                type = "multi_select",
                content = listOf(filterContent)
            )

            filterOperations.updateFilter(checkboxFilter)
        }

        checkboxAdapter.setOnCheckboxDeselectedListener { item ->
            filterOperations.removeFilterContent("checkbox_filter", item.lowercase().replace(" ", "_"))
        }
    }

    fun bind(category: FilterCategory, selectedItems: List<Any>?, isExpanded: Boolean) {
        filterTitle.tag = category
        filterTitle.text = "Categories" // Fixed title as per your request

        // Update expand/collapse icon
        expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expand_less 
            else R.drawable.ic_expand_more
        )
        
        // Show/hide content based on expanded state
        checkboxRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            val items = category.items.first().data as List<String>
            checkboxAdapter.submitList(items)
            selectedItems?.let {
                checkboxAdapter.setSelectedItems(it as List<String>)
            }
        }
    }

    fun clearSelection() {
        checkboxAdapter.clearSelection()
    }
}

