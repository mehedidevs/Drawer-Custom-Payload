package com.mehedi.filters_with_drawers_custom_payload.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehedi.filters_with_drawers_custom_payload.FilterCategory
import com.mehedi.filters_with_drawers_custom_payload.PriceRangeData
import com.mehedi.filters_with_drawers_custom_payload.R
import com.mehedi.filters_with_drawers_custom_payload.databinding.ItemPriceRangeFilterBinding

// Price Range Filter ViewHolder
class PriceRangeFilterViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_price_range_filter, parent, false)
) {
    private val binding = ItemPriceRangeFilterBinding.bind(itemView)
    private var currentPriceRange: PriceRangeData? = null

    fun bind(category: FilterCategory) {
        binding.filterTitle.text = category.name
        val priceData = category.items.first().data as PriceRangeData
        currentPriceRange = priceData

        binding.priceRangeSlider.apply {
            valueFrom = priceData.minPrice
            valueTo = priceData.maxPrice
            values = listOf(priceData.minPrice, priceData.maxPrice)

            addOnChangeListener { slider, _, _ ->
                val values = slider.values
                binding.priceRangeText.text = "₹${values[0].toInt()} - ₹${values[1].toInt()}"
                currentPriceRange = PriceRangeData(values[0], values[1])
            }
        }

        // Initial text setup
        binding.priceRangeText.text =
            "₹${priceData.minPrice.toInt()} - ₹${priceData.maxPrice.toInt()}"
    }

    fun getCurrentPriceRange(): PriceRangeData? = currentPriceRange
}
