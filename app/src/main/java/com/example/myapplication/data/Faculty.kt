package com.example.myapplication.data

import java.util.*

data class Faculty(
    val id : UUID = UUID.randomUUID(),
    var name : String="") {
    var groups: List<Group> = emptyList()
}
