package com.practise.taski_event.frags.allevent.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practise.taski_event.R
import com.practise.taski_event.base.BaseAppFragment
import com.practise.taski_event.repo.AppState
import com.practise.taski_event.data.Event
import com.practise.taski_event.data.EventListItem
import com.practise.taski_event.databinding.FragmentAllEventBinding
import com.practise.taski_event.frags.allevent.adopter.AllEventsRecyclerView
import com.practise.taski_event.frags.allevent.viewmodel.AllEventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllEventFragment : BaseAppFragment() {

    private val viewModel: AllEventViewModel by viewModels()
    private lateinit var binding: FragmentAllEventBinding

    override fun onStart() {
        super.onStart()
        viewModel.getEvents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.appState.observe(viewLifecycleOwner) { state ->
            if(state == AppState.Loading)
                showProgress()
            else
                hideProgress()
            when(state){
                is AppState.Events -> { bindDate(state.list) }
                AppState.Deleted -> {
                    viewModel.getEvents()
                    showToast(getString(R.string.event_removed))
                }
                else -> {}
            }
        }

        binding.apply {
            eventsView.layoutManager = LinearLayoutManager(requireContext())
            pageTitle.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun bindDate(events: List<Event>) {
        binding.apply {
            noEventFound.isVisible = events.isEmpty()
            val eventsByDate = events.groupBy { it.date }
            val flatList = flattenEvents(eventsByDate)
            eventsView.adapter = AllEventsRecyclerView(
                flatList,
                onEventClick =  { moveToEditEvent(it) },
                deleteEvent = { deleteEvent(it) }
            )
        }
    }

    private fun flattenEvents(map: Map<String, List<Event>>): List<EventListItem> {
        val flatList = mutableListOf<EventListItem>()
        map.toSortedMap().forEach { (date, events) ->
            flatList.add(EventListItem.DateHeader(date))
            flatList.addAll(events.map { EventListItem.EventItem(it) })
        }
        return flatList
    }

    private fun moveToEditEvent(event: Event?) {
        val action = AllEventFragmentDirections.actionAllEventFragmentToAddEventEventFragment(event)
        findNavController().navigate(action)
    }

    private fun deleteEvent(event: Event){
        viewModel.deleteEvent(event)
    }

}