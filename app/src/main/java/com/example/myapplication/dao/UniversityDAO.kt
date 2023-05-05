package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group

@Dao
interface UniversityDAO {
    //for faculty
    @Insert(entity = Faculty::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewFaculty(faculty: Faculty)

    @Query("DELETE FROM university WHERE id = :facultyID")
    fun deleteFacultyByID(facultyID : Long)

    @Delete(entity = Faculty::class)
    fun deleteFaculty(faculty: Faculty)

    @Query("SELECT id, first_name FROM university order by faculty_name")
    fun loadFaculty() : List<Faculty>

    @Update(entity = Faculty::class)
    fun updateFaculty(faculty: Faculty)
    //for group TODO:implement
    /*@Insert(entity = Group::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewGroup(group: Group)

    @Query("DELETE FROM university WHERE id = :facultyID")
    fun deleteGroupByID(facultyID : Long)

    @Delete(entity = Faculty::class)
    fun deleteGroup(faculty: Faculty)

    @Query("SELECT id, first_name FROM university order by faculty_name")
    fun loadGroup() : List<Faculty>

    @Update(entity = Faculty::class)
    fun updateGroup(faculty: Faculty)*/
    //for students TODO:implement

}