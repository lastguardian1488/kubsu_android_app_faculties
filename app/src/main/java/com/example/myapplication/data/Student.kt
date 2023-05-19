package com.example.myapplication.data

import androidx.room.*

@Entity(
    indices = [Index("last_name","first_name","middle_name")],
        foreignKeys = [
            ForeignKey(
                entity = Group::class,
                parentColumns = ["id"],
                childColumns = ["group_id"]
            )
        ]
)

data class Student(
    @PrimaryKey(autoGenerate = true) val id : Long?,
    @ColumnInfo(name = "first_name") var firstName : String?,
    @ColumnInfo(name = "last_name") var lastName : String?,
    @ColumnInfo(name = "middle_name") var middleName: String?,
    var phone : String?,
    @ColumnInfo(name = "birth_date") var birthDate : Long?,
    @ColumnInfo(name = "group_id") var groupID : Long?
)