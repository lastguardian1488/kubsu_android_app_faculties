package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student

@Dao
interface UniversityDAO {
    //for faculty
    @Insert(entity = Faculty::class/*, onConflict = OnConflictStrategy.REPLACE*/)
    fun insertNewFaculty(faculty: Faculty)

    @Query("DELETE FROM university WHERE id = :facultyID")
    fun deleteFacultyByID(facultyID : Long)

    @Delete(entity = Faculty::class)
    fun deleteFaculty(faculty: Faculty)

    @Query("SELECT id, first_name FROM university order by faculty_name")
    fun loadUniversity() : List<Faculty>

    @Update(entity = Faculty::class)
    fun updateFaculty(faculty: Faculty)

    @Query("SELECT id, faculty_name, from univesity where id = :id")
    fun getFaculty(id: Long) : Faculty

    //for group
    @Query("SELECT * FROM faculty where faculty_id = :facultyID order by group_name")
    fun loadFacultyGroup(facultyID: Long): List<Group>

    @Insert(entity = Group::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewGroup(group: Group)

    @Delete(entity = Group::class)
    fun deleteGroup(group: Group)

    @Query("SELECT * FROM faculty order by group_name")
    fun loadGroup() : List<Group>

    @Update(entity = Group::class)
    fun updateGroup(group: Group)

    //for students
    @Query("SELECT * FROM student where group_id = :groupID order by first_name")
    fun loadGroupStudents(groupID: Long): List<Student>
    @Insert(entity = Student::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertNewStudent(student: Student)

    @Delete(entity = Student::class)
    fun deleteStudent(student: Student)

    @Query("SELECT * FROM student order by last_name")
    fun loadStudent() : List<Student>

    @Update(entity = Student::class)
    fun updateStudent(student: Student)
}