package com.practise.taski_event.frags.event.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practise.taski_event.R
import com.practise.taski_event.base.BaseAppFragment
import com.practise.taski_event.repo.AppState
import com.practise.taski_event.data.Event
import com.practise.taski_event.databinding.FragmentEventBinding
import com.practise.taski_event.frags.event.adpoter.EventsRecyclerView
import com.practise.taski_event.frags.event.viewmodel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class EventFragment : BaseAppFragment() {

    private val viewModel: EventViewModel by viewModels()
    private lateinit var binding: FragmentEventBinding
    private var events : List<Event>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getEvents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.appState.observe(viewLifecycleOwner){ state ->
                if(state == AppState.Loading)
                    showProgress()
                else
                    hideProgress()

                when(state){
                    is AppState.Events -> {
                        events = state.list
                        mapData(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()), events ?: emptyList())
                    }
                    AppState.Deleted -> {
                        viewModel.getEvents()
                        showToast(getString(R.string.event_removed))
                    }
                    else -> {}
                }
            }

            viewAllEvents.setOnClickListener { findNavController().navigate(R.id.action_eventFragment_to_allEventFragment) }

            eventsView.layoutManager = LinearLayoutManager(requireContext())

            calendar.setOnDateChangeListener { _, year, month, date ->
                String.format("%02d/%02d/%04d", date, month + 1, year).apply {
                    mapData(this, events ?: emptyList())
                }
            }

            addEvent.setOnClickListener { moveToEditEvent(null) }
        }
    }

    private fun moveToEditEvent(event: Event?) {
        val action = EventFragmentDirections.actionEventFragmentToAddEventEventFragment(event)
        findNavController().navigate(action)
    }

    private fun mapData(date: String?,data: List<Event>) {
        val eventByDates : Map<String, List<Event>> = data.groupBy { it.date }
        val eventOnDate = eventByDates[date] ?: emptyList()
        binding.apply {
            noEventFound.isVisible = eventOnDate.isEmpty()
            eventsView.adapter = EventsRecyclerView(
                eventOnDate,
                onEventClick =  {  event, -> moveToEditEvent(event) },
                deleteEvent = {event: Event -> deleteEvent(event) }
            )
        }
    }

    private fun deleteEvent(event: Event){
        viewModel.deleteEvent(event)
    }

}