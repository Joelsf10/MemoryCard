package com.uni.memorycard.data.local

import androidx.room.TypeConverter
import com.uni.memorycard.ui.model.GameConfiguration
import java.util.Date


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}