package com.github.atlamp2023.wpreader.features.list.presentation

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.atlamp2023.wpreader.databinding.ItemListBinding

class ListAdapter(private val listener: Listener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    var list = listOf<ListItem>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
            //Log.d("main", "${field}")
        }

    interface Listener {
        fun clickItem(data: ListItem, pos: Int)
    }

    inner class ListItemsHolder(val binding: ItemListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListItem) = with(binding) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvTitle.text =  Html.fromHtml(data.title ?: "", Html.FROM_HTML_MODE_COMPACT)
                tvPreview.text = Html.fromHtml(data.preview ?: "", Html.FROM_HTML_MODE_COMPACT)
            } else {
                tvTitle.text =  Html.fromHtml(data.title ?: "")
                tvPreview.text = Html.fromHtml(data.preview ?: "")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)
        binding.tvPreview.setOnClickListener(this)
        return ListItemsHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = list[position]
        (holder as ListItemsHolder).binding.tvPreview.tag = Pair(data, position)
        (holder as ListItemsHolder).bind(data)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onClick(v: View?) {
        val p = v?.tag as Pair<*, *>
        listener.clickItem(p.first as ListItem, p.second as Int)
    }
}