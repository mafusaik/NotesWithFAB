package by.homework.hlazarseni.noteswithfab.utils


import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun currentDateTime(): String {
  val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm")
  } else {
    TODO("VERSION.SDK_INT < O")
  }
  return LocalDateTime.now().format(formatter)
}

fun currentDate(): String {
  val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    DateTimeFormatter.ofPattern("dd.MM.yyyy")
  } else {
    TODO("VERSION.SDK_INT < O")
  }
  return LocalDate.now().format(formatter)
}

fun currentTime(): String {
  val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    DateTimeFormatter.ofPattern("hh:mm")
  } else {
    TODO("VERSION.SDK_INT < O")
  }
  return LocalTime.now().format(formatter)
}