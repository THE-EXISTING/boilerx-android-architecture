package com.existing.boilerx.ktx

/**
 * Created by「 The Khaeng 」on 13 Apr 2018 :)
 */
import java.util.*

val timestamp: Long get() = System.currentTimeMillis()

fun Long.toDate(): Date = Date(this * 1000L)

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) = set(Calendar.YEAR, value)

var Calendar.month: Int
    get() = get(Calendar.MONTH)
    set(value) = set(Calendar.MONTH, value)

var Calendar.weekInMonth: Int
    get() = get(Calendar.WEEK_OF_MONTH)
    set(value) = set(Calendar.WEEK_OF_MONTH, value)

var Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) = set(Calendar.DAY_OF_MONTH, value)

var Calendar.dayOfWeek: Int
    get() = get(Calendar.DAY_OF_WEEK)
    set(value) = set(Calendar.DAY_OF_WEEK, value)

var Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) = set(Calendar.HOUR_OF_DAY, value)

var Calendar.minute: Int
    get() = get(Calendar.MINUTE)
    set(value) = set(Calendar.MINUTE, value)

var Calendar.second: Int
    get() = get(Calendar.SECOND)
    set(value) = set(Calendar.SECOND, value)

var Calendar.millisecond: Int
    get() = get(Calendar.MILLISECOND)
    set(value) = set(Calendar.MILLISECOND, value)


fun Date.toFormat(format: String, local: Locale = Locale.getDefault()): String {
    val format = java.text.SimpleDateFormat(format, local)
    return format.format(this)
}

fun Date.toFormatAgo(local: Locale = Locale.getDefault()): String {
    return getTimeAgo(this.time)
}


private val SECOND_MILLIS = 1000
private val MINUTE_MILLIS = 60 * SECOND_MILLIS
private val HOUR_MILLIS = 60 * MINUTE_MILLIS
private val DAY_MILLIS = 24 * HOUR_MILLIS

private fun getTimeAgo(time: Long): String {
    var timeMillis = time
    if (time < 1000000000000L) {
        // if timestamp given in seconds, convert to millis
        timeMillis *= 1000
    }

    val now = Calendar.getInstance().timeInMillis
    if (time > now || time <= 0) {
        return ""
    }

    // TODO: localize
    val diff = now - timeMillis
    return when {
        diff < MINUTE_MILLIS -> "just now"
        diff < 2 * MINUTE_MILLIS -> "a min\nago"
        diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " min\nago"
        diff < 90 * MINUTE_MILLIS -> "an hour\nago"
        diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " hours\nago"
        diff < 48 * HOUR_MILLIS -> "yesterday"
        else -> (diff / DAY_MILLIS).toString() + " days"
    }
}

