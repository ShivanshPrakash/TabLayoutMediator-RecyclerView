package com.shivansh.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.shivansh.tablayoutrecyclerviewmediator.TabLayoutRecyclerViewMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerViewWithTabs()
    }

    private fun setupRecyclerViewWithTabs() {
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        // Data
        val itemList = listOf("Asia", "India", "China", "Pakistan", "Japan",
            "Africa", "South Africa", "Egypt", "Zimbabwe", "Kenya",
            "Europe", "France", "Russia", "Germany", "Italy", "United Kingdom")

        val headerList = listOf("Asia", "Africa", "Europe")

        val isHeaderList = listOf(true, false, false, false, false, true, false, false, false, false, true,
            false, false, false, false, false)


        // Setup recycler view
        val adapter = TabsRecyclerAdapter(itemList, isHeaderList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // processing data to map recyclerView items to tabs in tabLayout
        val headerMap = setHeaderMap(isHeaderList)
        // processing data to map tabs in tabLayout to view index of recyclerView
        val itemMap = setItemMap(isHeaderList)

        // Attach tabLayout to recyclerView
        val headerToItemMapping = { position: Int -> itemMap[position] }
        val itemToHeaderMapping = { position: Int -> headerMap[position] }
        TabLayoutRecyclerViewMediator(recyclerView, headerList.size, tabLayout, headerToItemMapping, itemToHeaderMapping) { tab, position ->
            tab.text = headerList[position]
        }.attach()
    }

    private fun setItemMap(isHeader: List<Boolean>): Map<Int, Int> {
        val itemMap = mutableMapOf<Int, Int>()
        var headerIndex = -1
        for ((itemCount, hIndex) in isHeader.indices.withIndex()) {
            if (isHeader[hIndex]) {
                headerIndex++
                itemMap[headerIndex] = itemCount
            }
        }
        return itemMap
    }

    private fun setHeaderMap(headerList: List<Boolean>): Map<Int, Int> {
        var currentHeader = -1
        val headerMap = mutableMapOf<Int, Int>()
        for (index in headerList.indices) {
            if (headerList[index]) currentHeader++
            headerMap[index] = currentHeader
        }
        return headerMap
    }
}