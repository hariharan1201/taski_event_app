package com.practise.taski_event.frags.allevent.adopter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practise.taski_event.data.Event
import com.practise.taski_event.data.EventListItem
import com.practise.taski_event.databinding.EventDateHeaderBinding
import com.practise.taski_event.databinding.EventListTileBinding

class AllEventsRecyclerView (private val list: List<EventListItem>,private val onEventClick: (Event) -> Unit, private val deleteEvent : (Event)-> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class EventView(val binding: EventListTileBinding) : RecyclerView.ViewHolder(binding.root)
    inner class DateHeader(val binding: EventDateHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when(list[position]){
            is EventListItem.DateHeader -> 0
            is EventListItem.EventItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 1){
            val binding = EventListTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EventView(binding)
        } else {
            val binding = EventDateHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DateHeader(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = list[position]){
            is EventListItem.EventItem -> {
                val eventView = holder as EventView
                eventView.binding.apply {
                    title.text = item.event.title
                    time.text = item.event.time
                    date.text = item.event.date
                    eventTile.setOnClickListener { onEventClick(item.event) }
                    deleteBtn.setOnClickListener { deleteEvent(item.event) }
                }
            }
            is EventListItem.DateHeader -> {
                val dateView = holder as DateHeader
                dateView.binding.apply {
                    date.text = item.date
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

}