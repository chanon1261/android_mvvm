package com.panat.mvvm.retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panat.mvvm.retrofit.databinding.ItemEventsBinding
import com.panat.mvvm.retrofit.model.GithubEvents

class GitEventsAdapter(private val context: Context) :
    RecyclerView.Adapter<GitEventsAdapter.EventsAapterViewHolder>() {

    private var items: MutableList<GithubEvents> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAapterViewHolder {
        val layoutInflator: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return EventsAapterViewHolder(
            ItemEventsBinding.inflate(
                layoutInflator,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsAapterViewHolder, position: Int) =
        holder.bind(items[position])

    class EventsAapterViewHolder(private val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context

        fun bind(item: GithubEvents) {
            binding.name.text = item.actor.display_login
            binding.event.text = item.type
            Glide.with(context).load(item.actor.avatar_url).into(binding.profileImg)
        }
    }

    fun loadData(data: List<GithubEvents>) {
        this.items = data as MutableList<GithubEvents>
        notifyDataSetChanged()
    }

    fun add(data: List<GithubEvents>) {
        this.items.addAll(data)
    }
}
