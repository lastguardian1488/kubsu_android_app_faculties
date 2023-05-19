package com.example.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "university")
data class Faculty(
    @PrimaryKey(autoGenerate = true) val id : Long?,
    @ColumnInfo(name = "faculty_name") val name : String?
)