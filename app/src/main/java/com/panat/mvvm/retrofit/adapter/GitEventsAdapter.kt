package com.panat.mvvm.retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.panat.mvvm.retrofit.databinding.ItemEventsBinding
import com.panat.mvvm.retrofit.extension.loadUrl
import com.panat.mvvm.retrofit.model.GitEvent.GithubEvents

class GitEventsAdapter(private val context: Context) :
    RecyclerView.Adapter<GitEventsAdapter.EventsAdapterViewHolder>() {

    private var items: MutableList<GithubEvents> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapterViewHolder {
        val layoutInflator: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return EventsAdapterViewHolder(
            ItemEventsBinding.inflate(
                layoutInflator,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsAdapterViewHolder, position: Int) =
        holder.bind(items[position])

    class EventsAdapterViewHolder(private val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GithubEvents) {
            binding.name.text = item.actor.display_login
            binding.event.text = item.type
            binding.profileImg.loadUrl(item.actor.avatar_url)
        }
    }

    fun loadData(data: List<GithubEvents>) {
        this.items = data as MutableList<GithubEvents>
        notifyDataSetChanged()
    }

}
