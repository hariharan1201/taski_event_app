package com.practise.taski_event.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int? = System.currentTimeMillis().toInt(),
    val title: String,
    val description: String,
    val date: String,
    val time: String
) : Parcelable
