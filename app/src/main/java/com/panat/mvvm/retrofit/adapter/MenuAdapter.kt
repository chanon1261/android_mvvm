package com.panat.mvvm.retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.panat.mvvm.retrofit.databinding.ItemMenuBinding

class MenuAdapter(private val context: Context, private val listener: OnClickItem) :
    RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder>() {

    private var items: List<String> = mutableListOf()

    interface OnClickItem {
        fun onclick(position: Int)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapterViewHolder {
        val layoutInflator: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return MenuAdapterViewHolder(
            ItemMenuBinding.inflate(
                layoutInflator,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuAdapterViewHolder, position: Int) =
        holder.bind(items[position])

    inner class MenuAdapterViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.menuName.text = item
            itemView.setOnClickListener {
                listener.onclick(adapterPosition)
            }
        }
    }

    fun loadData(data: List<String>) {
        this.items = data
        notifyDataSetChanged()
    }
}