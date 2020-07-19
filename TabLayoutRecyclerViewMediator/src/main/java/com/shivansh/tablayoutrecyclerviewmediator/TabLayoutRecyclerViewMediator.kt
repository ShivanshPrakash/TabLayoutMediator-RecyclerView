package com.shivansh.tablayoutrecyclerviewmediator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

/**
 * Created by Shivansh ON 13/07/20.
 */
class TabLayoutRecyclerViewMediator(private val recyclerView: RecyclerView,
                                    private val headerCount: Int,
                                    private val tabLayout: TabLayout,
                                    private val mapHeaderPositionToItemPosition: (itemPosition: Int) -> Int?,
                                    private val mapItemPositionToHeaderPosition: (itemPosition: Int) -> Int?,
                                    private val autoRefresh: Boolean = true,
                                    private val tabConfigurationStrategy: (tab: TabLayout.Tab, position: Int) -> Unit) {

    private var isProgrammaticallyScrolled = false
    private var recyclerAdapterObserver: RecyclerView.AdapterDataObserver? = null
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var tabSelectedListener: TabLayout.OnTabSelectedListener? = null

    fun attach() {
        if (autoRefresh && recyclerView.adapter != null) {
            recyclerAdapterObserver = object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    populateTabsLayout()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    populateTabsLayout()
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    populateTabsLayout()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    populateTabsLayout()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    populateTabsLayout()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    populateTabsLayout()
                }
            }
            recyclerView.adapter!!.registerAdapterDataObserver(recyclerAdapterObserver as RecyclerView.AdapterDataObserver)
        }
        onScrollListener = TabLayoutOnPageChangeCallback()
        tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val itemPosition = tab?.position?.let { mapHeaderPositionToItemPosition(it) }
                if (itemPosition != null) {
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }
                    }
                    smoothScroller.targetPosition = itemPosition
                    isProgrammaticallyScrolled = true
                    recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        }

        recyclerView.addOnScrollListener(onScrollListener as TabLayoutOnPageChangeCallback)
        tabLayout.addOnTabSelectedListener(tabSelectedListener as TabLayout.OnTabSelectedListener)
        populateTabsLayout()
    }

    fun detach() {
        onScrollListener?.let { recyclerView.removeOnScrollListener(it) }
        tabSelectedListener?.let { tabLayout.removeOnTabSelectedListener(it) }
        recyclerAdapterObserver?.let { recyclerView.adapter?.unregisterAdapterDataObserver(it) }

        onScrollListener = null
        tabSelectedListener = null
        recyclerAdapterObserver = null
    }

    private fun populateTabsLayout() {
        tabLayout.removeAllTabs()
        for (index in 0 until headerCount) {
            val newTab = tabLayout.newTab()
            tabConfigurationStrategy.invoke(newTab, index)
            tabLayout.addTab(newTab, false)
        }
    }

    private inner class TabLayoutOnPageChangeCallback : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (recyclerView.layoutManager is LinearLayoutManager && !isProgrammaticallyScrolled) {
                var position = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (position < 0) position = 0
                val tabPosition = mapItemPositionToHeaderPosition(position)
                if (tabPosition != null) {
                    tabLayout.setScrollPosition(tabPosition, 0f, true, true)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) isProgrammaticallyScrolled = false
        }
    }
}