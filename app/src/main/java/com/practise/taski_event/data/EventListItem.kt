package com.practise.taski_event.data

sealed class EventListItem {
    data class DateHeader(val date: String) : EventListItem()
    data class EventItem(val event: Event) : EventListItem()
}
