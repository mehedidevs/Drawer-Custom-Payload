package com.mehedi.filters_with_drawers_custom_payload

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mehedi.filters_with_drawers_custom_payload.databinding.ActivityMainBinding


// Main Activity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var filterRecyclerView: RecyclerView
    private var filterAdapter: FilterAdapter? = null
    private val filterManager = FilterManager

    // Create sample filter data
    val filters = createSampleFilters()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        setupFilters()
        setupFilterButton()
    }

    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun setupFilters() {
        filterRecyclerView = binding.filterRecyclerView
        filterRecyclerView.layoutManager = LinearLayoutManager(this)


        FilterManager.addListener { filters ->
            Log.d("Filters", "Filter state updated: ${filters.size} filters")
            filters.forEach { (id, filter) ->
                Log.d("Filters", "Filter $id has ${filter.content.size} items")
            }
        }


        buildAdapter()


    }

    private fun buildAdapter() {
        filterAdapter = null
        filterAdapter = FilterAdapter(filters) { filter ->
            // Handle filter changes
            when (filter.type) {
                FilterType.COLOR -> handleColorFilter(filter)
                FilterType.SIZE -> handleSizeFilter(filter)
                FilterType.PRICE_RANGE -> handlePriceFilter(filter)
                FilterType.CHECKBOX -> handleCheckboxFilter(filter)
                FilterType.RADIO -> handleRadioFilter(filter)
            }
        }

        filterRecyclerView.adapter = filterAdapter
    }

    private fun createSampleFilters(): List<FilterCategory> {
        return listOf(
            // Color Filter Category
            FilterCategory(
                id = "colors",
                name = "Colors",
                items = listOf(
                    FilterItem(
                        id = "color_filter",
                        title = "Available Colors",
                        type = FilterType.COLOR,
                        data = listOf(
                            ColorData("Red", "#FF0000"),
                            ColorData("Blue", "#0000FF"),
                            ColorData("Green", "#00FF00"),
                            ColorData("Black", "#000000"),
                            ColorData("White", "#FFFFFF")
                        )
                    )
                )
            ),
            // Size Filter Category
            FilterCategory(
                id = "sizes",
                name = "Sizes",
                items = listOf(
                    FilterItem(
                        id = "size_filter",
                        title = "Available Sizes",
                        type = FilterType.SIZE,
                        data = listOf(
                            SizeData("Small", "S"),
                            SizeData("Medium", "M"),
                            SizeData("Large", "L"),
                            SizeData("X-Large", "XL")
                        )
                    )
                )
            ),
            // Price Range Filter Category
            FilterCategory(
                id = "price",
                name = "Price Range",
                items = listOf(
                    FilterItem(
                        id = "price_filter",
                        title = "Select Price Range",
                        type = FilterType.PRICE_RANGE,
                        data = PriceRangeData(0f, 1000f)
                    )
                )
            ),
            // Category Filter
            FilterCategory(
                id = "categories",
                name = "Categories",
                items = listOf(
                    FilterItem(
                        id = "category_filter",
                        title = "Product Categories",
                        type = FilterType.CHECKBOX,
                        data = listOf(
                            "Electronics",
                            "Clothing",
                            "Books",
                            "Home & Kitchen"
                        )
                    )
                )
            )
        )
    }


    private fun setupFilterButton() {
        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Setup apply filters button
        binding.applyFiltersButton.setOnClickListener {
            applyFilters()
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        // Setup clear filters button
        binding.clearFiltersButton.setOnClickListener {
            filterManager.clearFilters()
            filterAdapter?.clearSelections()
            Snackbar.make(
                binding.root,
                "All filters cleared",
                Snackbar.LENGTH_SHORT
            ).show()
            buildAdapter()

        }
    }

    private fun handleColorFilter(filter: FilterItem) {
        val colorData = filter.data as List<ColorData>
        colorData.forEach { color ->
            val filterContent = FilterContent(
                id = color.colorName,
                title = color.colorName,
                color = color.hexCode,
                icon = null
            )

            val colorFilter = Filter(
                id = "color_filter",
                title = "Colors",
                from = "ATTRIBUTE",
                type = "single_select",
                content = listOf(filterContent)
            )

            filterManager.updateFilter(colorFilter)
        }
    }

    private fun handleSizeFilter(filter: FilterItem) {
        val sizeData = filter.data as List<SizeData>
        sizeData.forEach { size ->
            val filterContent = FilterContent(
                id = size.sizeCode,
                title = size.sizeName,
                color = null,
                icon = null
            )

            val sizeFilter = Filter(
                id = "size_filter",
                title = "Sizes",
                from = "ATTRIBUTE",
                type = "single_select",
                content = listOf(filterContent)
            )

            filterManager.updateFilter(sizeFilter)
        }
    }

    private fun handlePriceFilter(filter: FilterItem) {
        val priceData = filter.data as PriceRangeData
        val filterContent = FilterContent(
            id = "price_range",
            title = "₹${priceData.minPrice} - ₹${priceData.maxPrice}",
            color = null,
            icon = null
        )

        val priceFilter = Filter(
            id = "price_filter",
            title = "Price Range",
            from = "ATTRIBUTE",
            type = "range",
            content = listOf(filterContent)
        )

        filterManager.updateFilter(priceFilter)
    }

    private fun handleCheckboxFilter(filter: FilterItem) {
        val categories = filter.data as List<String>
        val filterContents = categories.map { category ->
            FilterContent(
                id = category.lowercase().replace(" ", "_"),
                title = category,
                color = null,
                icon = null
            )
        }

        val categoryFilter = Filter(
            id = "category_filter",
            title = "Categories",
            from = "ATTRIBUTE",
            type = "multi_select",
            content = filterContents
        )

        filterManager.updateFilter(categoryFilter)
    }

    private fun handleRadioFilter(filter: FilterItem) {
        val options = filter.data as List<String>
        val filterContents = options.map { option ->
            FilterContent(
                id = option.lowercase().replace(" ", "_"),
                title = option,
                color = null,
                icon = null
            )
        }

        val radioFilter = Filter(
            id = filter.id,
            title = filter.title,
            from = "ATTRIBUTE",
            type = "single_select",
            content = filterContents
        )

        filterManager.updateFilter(radioFilter)
    }

    // Update your Activity's applyFilters method
    private fun applyFilters() {
        val activeFilters = FilterManager.getActiveFilters()

        val payload = FilterPayload(
            keyword = "binding.searchEditText.text?.toString() " ?: "",
            categorySlug = "furniture-appliances-18",  // Get this from your current category
            filters = activeFilters.toMutableList(),
            sortBy = "relevance"  // Update this based on user's sort selection
        )

        Log.d("Filters", "Applying filters: ${payload.filters.size} filters")
        payload.filters.forEach { filter ->
            Log.d("Filters", "Filter: ${filter.id}, Content: ${filter.content.size} items")
        }
        Log.d("Filters", "payload: $payload")

        // Make your API call with the payload
        // viewModel.applyFilters(payload)
    }


    private fun makeApiCall(payload: FilterPayload) {
        // Implementation of API call
        /* viewModelScope.launch {
             try {
                 // Example API call
                 val response = api.applyFilters(payload)
                 // Handle response
                 handleApiResponse(response)
             } catch (e: Exception) {
                 // Handle error
                 handleError(e)
             }
         }*/
    }

    private fun handleApiResponse(response: Any) {
        // Update UI with filtered results
        // Example:
        /*       binding.productRecyclerView.submitList(response.products)
               binding.totalResults.text = "${response.totalResults} results found"*/
    }

    private fun handleError(error: Exception) {
        // Show error message
        Snackbar.make(
            binding.root,
            "Error applying filters: ${error.localizedMessage}",
            Snackbar.LENGTH_LONG
        ).show()
    }
}