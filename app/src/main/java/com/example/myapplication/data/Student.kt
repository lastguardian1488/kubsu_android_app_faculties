package com.example.myapplication.data

import java.util.*

data class Student(
    val id : UUID = UUID.randomUUID(),
    var firstName : String="",
    var lastName : String="",
    var middleName : String="",
    var phone : String="",
    var birthDate : Date = Date(0L)
)