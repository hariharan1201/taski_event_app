package com.practise.taski_event.frags.event.adpoter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practise.taski_event.data.Event
import com.practise.taski_event.databinding.EventListTileBinding

class EventsRecyclerView(private val events: List<Event>, private val onEventClick: (Event) -> Unit, private val deleteEvent : (Event)-> Unit) :
    RecyclerView.Adapter<EventsRecyclerView.EventHolder>() {

    inner class EventHolder(val binding: EventListTileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding = EventListTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = events[position]
        holder.binding.apply {
            title.text = event.title
            time.text = event.time
            date.text = event.date
            eventTile.setOnClickListener { onEventClick(event) }
            deleteBtn.setOnClickListener { deleteEvent(event) }
        }
    }
}
