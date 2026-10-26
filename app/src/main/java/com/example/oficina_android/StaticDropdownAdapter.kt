package com.example.oficina_android

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class StaticDropdownAdapter(
    context: Context,
    options: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, options) {

    private val allOptions = options.toList()
    private val unfilteredOptions = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults = FilterResults().apply {
            values = allOptions
            count = allOptions.size
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if ((results?.count ?: 0) > 0) notifyDataSetChanged() else notifyDataSetInvalidated()
        }
    }

    override fun getFilter(): Filter = unfilteredOptions
}
