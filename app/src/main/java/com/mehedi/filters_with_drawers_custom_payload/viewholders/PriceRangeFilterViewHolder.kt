package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.PriceRangeData
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.databinding.ItemPriceRangeFilterBinding

class PriceRangeFilterViewHolder(
    parent: ViewGroup,
    private val onTitleClick: (String) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_price_range_filter, parent, false)
) {
    private val binding = ItemPriceRangeFilterBinding.bind(itemView)
    private val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
    private var currentPriceRange: PriceRangeData? = null

    init {
        binding.filterTitle.setOnClickListener {
            val category = binding.filterTitle.tag as FilterCategory
            onTitleClick(category.id)
        }
    }

    fun bind(category: FilterCategory, isExpanded: Boolean) {
        binding.filterTitle.tag = category
        binding.filterTitle.text = category.name
        
        // Update expand/collapse icon
        expandIcon.setImageResource(
            if (isExpanded) R.drawable.ic_expand_less 
            else R.drawable.ic_expand_more
        )
        
        // Show/hide content based on expanded state
        binding.priceRangeContent.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            val priceData = category.items.first().data as PriceRangeData
            currentPriceRange = priceData
            
            // Update your price range UI components here
            binding.priceRangeSlider.valueFrom = priceData.minPrice.toFloat()
            binding.priceRangeSlider.valueTo = priceData.maxPrice.toFloat()
            // ... other price range specific bindings
        }
    }

    fun clearSelection() {
        // Reset price range selection if needed
        currentPriceRange?.let { priceRange ->
            binding.priceRangeSlider.values = listOf(
                priceRange.minPrice.toFloat(),
                priceRange.maxPrice.toFloat()
            )
        }
    }
}
