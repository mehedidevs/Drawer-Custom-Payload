package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.mehedi.filters_with_drawers_custom_payload.Filter
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.FilterContent
import com.mehedi.filters_with_drawers_custom_payload.FilterManager
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.adapters.CheckboxAdapter
import com.mehedi.filters_with_drawers_custom_payload.adapters.CheckboxDiffCallback
import com.mehedi.filters_with_drawers_custom_payload.databinding.ItemCheckboxFilterBinding


// Checkbox Filter ViewHolder and Adapter
class CheckboxFilterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_checkbox_filter, parent, false)
) {
    private val filterTitle: TextView = itemView.findViewById(R.id.filterTitle)
    private val checkboxRecyclerView: RecyclerView =
        itemView.findViewById(R.id.checkboxRecyclerView)
    private val checkboxAdapter = CheckboxAdapter()

    init {
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

            FilterManager.updateFilter(checkboxFilter)
        }

        checkboxAdapter.setOnCheckboxDeselectedListener { item ->
            FilterManager.removeFilterContent("checkbox_filter", item.lowercase().replace(" ", "_"))
        }
    }

    fun bind(category: FilterCategory, selectedItems: List<Any>?) {
        filterTitle.text = "Categories" // Fixed title as per your request

        val items = category.items.first().data as List<String>
        checkboxAdapter.submitList(items)
        selectedItems?.let {
            checkboxAdapter.setSelectedItems(it as List<String>)
        }
    }
}

