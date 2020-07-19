package com.shivansh.sampleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Shivansh ON 13/07/20.
 */
const val TYPE_HEADER = 0
const val TYPE_ITEM = 1

class TabsRecyclerAdapter(private val data: List<String>, private val headerCheck: List<Boolean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) TabsHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_header, parent, false))
        else TabsItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.country_card, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM)
            (holder as TabsItemViewHolder).managerName.text = data[position]
        else (holder as TabsHeaderViewHolder).headerText.text = data[position]
    }

    class TabsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val managerName: TextView = itemView.findViewById(R.id.country_name)
    }

    class TabsHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerText: TextView = itemView.findViewById(R.id.text_header)
    }

    override fun getItemViewType(position: Int): Int =
        if (headerCheck[position]) TYPE_HEADER else TYPE_ITEM
}
