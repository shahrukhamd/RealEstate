package com.shahrukhamd.realestate.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahrukhamd.realestate.R
import com.shahrukhamd.realestate.data.model.PropertyItem
import com.shahrukhamd.realestate.databinding.ViewPropertyListItemBinding
import com.shahrukhamd.realestate.ui.RealEstateViewModel


class PropertyListAdapter(
    private val viewModel: RealEstateViewModel
) : ListAdapter<PropertyItem, PropertyListAdapter.ViewHolder>(PropertyListDiffUtil()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ViewPropertyListItemBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_property_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding?.also {
            it.property = item
            it.viewModel = viewModel
        }
    }
}

class PropertyListDiffUtil : DiffUtil.ItemCallback<PropertyItem>() {
    override fun areItemsTheSame(oldItem: PropertyItem, newItem: PropertyItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PropertyItem, newItem: PropertyItem): Boolean {
        return oldItem == newItem
    }
}