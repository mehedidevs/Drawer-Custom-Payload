package com.mehedi.filters_with_drawers_custom_payload


import FilterOperations
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mehedi.filters_with_drawers_custom_payload.databinding.ActivityMainBinding
import com.mehedi.filters_with_drawers_custom_payload.factory.FilterHandlerFactory


// Main Activity
class MainActivity : AppCompatActivity() {
    // View Binding instance for accessing layout views
    private lateinit var binding: ActivityMainBinding
    
    // UI Components
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var filterRecyclerView: RecyclerView
    private var filterAdapter: FilterAdapter? = null
    
    // Filter Management Components
    private val filterManager = FilterManager  // Singleton object that handles filter state
    private lateinit var filterHandlerFactory: FilterHandlerFactory  // Factory for creating filter handlers
    private lateinit var filters: List<FilterCategory>  // List of all available filters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize components
        filters = createSampleFilters()  // Create sample filter data
        filterHandlerFactory = FilterHandlerFactory(filterManager)  // Initialize factory with filter manager
        
        // Setup UI components
        setupDrawer()
        setupFilters()
        setupFilterButton()
    }

    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout

        // Configure toolbar with drawer toggle
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    private fun setupFilters() {
        // Setup RecyclerView for filters
        filterRecyclerView = binding.filterRecyclerView
        filterRecyclerView.layoutManager = LinearLayoutManager(this)

        // Add listener to monitor filter changes
        FilterManager.addListener { filters ->
            Log.d("Filters", "Filter state updated: ${filters.size} filters")
            filters.forEach { (id, filter) ->
                Log.d("Filters", "Filter $id has ${filter.content.size} items")
            }
        }

        buildAdapter()
    }

    private fun buildAdapter() {
        // Create new adapter instance to avoid state issues
        filterAdapter = null
        filterAdapter = FilterAdapter(
            categories = filters,  // List of filter categories
            onFilterChanged = { filter -> handleFilter(filter) },  // Callback for filter changes
            filterHandlerFactory = filterHandlerFactory,  // Factory for creating handlers
            filterOperations = filterManager  // Operations for managing filters
        )

        filterRecyclerView.adapter = filterAdapter
    }

    private fun setupFilterButton() {
        // Configure navigation drawer toggle
        binding.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Setup apply filters button
        binding.applyFiltersButton.setOnClickListener {
            applyFilters()  // Apply current filters
            drawerLayout.closeDrawer(GravityCompat.END)  // Close drawer
        }

        // Setup clear filters button
        binding.clearFiltersButton.setOnClickListener {
            filterManager.clearFilters()  // Clear all filters
            filterAdapter?.clearSelections()  // Clear UI selections
            Snackbar.make(
                binding.root,
                "All filters cleared",
                Snackbar.LENGTH_SHORT
            ).show()
            buildAdapter()  // Rebuild adapter to reflect cleared state
        }
    }

    private fun handleFilter(filter: FilterItem) {
        // Create appropriate handler for filter type and handle the filter
        val handler = filterHandlerFactory.createHandler(filter.type)
        handler.handleFilter(filter)
    }

    private fun applyFilters() {
        // Get current active filters
        val activeFilters = FilterManager.getActiveFilters()

        // Create payload for API call
        val payload = FilterPayload(
            keyword = "binding.searchEditText.text?.toString() " ?: "",
            categorySlug = "furniture-appliances-18",  // Category identifier
            filters = activeFilters.toMutableList(),
            sortBy = "relevance"  // Default sort option
        )

        // Log filter application for debugging
        Log.d("Filters", "Applying filters: ${payload.filters.size} filters")
        payload.filters.forEach { filter ->
            Log.d("Filters", "Filter: ${filter.id}, Content: ${filter.content.size} items")
        }
        Log.d("Filters", "payload: $payload")

        // TODO: Make API call with payload
    }

    // Sample data creation for testing
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
            // Additional filter categories...
        )
    }

    // Helper methods for API interaction
    private fun makeApiCall(payload: FilterPayload) {
        // TODO: Implement API call
    }

    private fun handleApiResponse(response: Any) {
        // TODO: Handle API response
    }

    private fun handleError(error: Exception) {
        // Show error message to user
        Snackbar.make(
            binding.root,
            "Error applying filters: ${error.localizedMessage}",
            Snackbar.LENGTH_LONG
        ).show()
    }
}