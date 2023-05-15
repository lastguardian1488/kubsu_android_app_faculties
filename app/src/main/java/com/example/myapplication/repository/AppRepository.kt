package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.myapplication.Second352_2023Application
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import com.example.myapplication.database.UniversityDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

const val SHARED_PREFERENCES_NAME = "UniversityAppPrefs"

class AppRepository private constructor() {
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()
    var faculty : MutableLiveData<List<Group>> = MutableLiveData()

    companion object {
        private var INSTANCE: AppRepository? = null

        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = AppRepository()
            }
        }

        fun get(): AppRepository {
            return INSTANCE ?: throw IllegalAccessException("Репозиторий не инициализирован")
        }
    }

    //тут дб
    val db = Room.databaseBuilder(
        Second352_2023Application.applicationContext(),
        UniversityDatabase::class.java, "facultiesApp.db"
    ).build()

    val universityDAO = db.getDao()

    /*fun saveData(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val jsonUniversity = Gson().toJson(university.value)
        sharedPreferences.edit().putString(SHARED_PREFERENCES_NAME, jsonUniversity).apply()
    }*/

    /*fun loadData(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(SHARED_PREFERENCES_NAME, null)
        if (jsonString != null) {
            val listType = object : TypeToken<List<Faculty>>() {}.type
            val faculties = Gson().fromJson<List<Faculty>>(jsonString, listType)
            university.value = faculties
        } else {
            university.value = arrayListOf()
        }
    }*/

    suspend fun newFaculty(name: String) {
        val faculty = Faculty(id = null, name = name)
        withContext(Dispatchers.IO) {
            universityDAO.insertNewFaculty(faculty)
            university.postValue(universityDAO.loadUniversity())
        }
    }

    suspend fun newGroup(facultyID: Long, name: String) {
        val group = Group(id = null, name = name, facultyID=facultyID)
        withContext(Dispatchers.IO) {
            universityDAO.insertNewGroup(group)
            getFacultyGroups(facultyID)
        }
    }

    suspend fun loadFaculty() {
        withContext(Dispatchers.IO) {
            university.postValue(universityDAO.loadUniversity())
        }
    }

    suspend fun getFacultyGroups(facultyID : Long) {
        withContext(Dispatchers.IO) {
            faculty.postValue(universityDAO.loadFacultyGroup(facultyID))
        }
    }

    suspend fun getFaculty(facultyID: Long) : Faculty? {
        var f : Faculty? = null
        val job = CoroutineScope(Dispatchers.IO).launch {
            f = universityDAO.getFaculty(facultyID)
        }
        job.join()
        return f
    }

    suspend fun getGroupStudents(groupID : Long) : List<Student>{
        var f : List<Student> = emptyList()
        val job = CoroutineScope(Dispatchers.IO).launch {
            f = universityDAO.loadGroupStudents(groupID)
        }
        job.join()
        return f
    }

    /*fun newGroup(facultyID: UUID, name: String) {
        val u = university.value ?: return
        val faculty = u.find { it.id == facultyID } ?: return
        val group = Group(name = name)
        val list: ArrayList<Group> = if (faculty.groups.isEmpty())
            ArrayList()
        else
            faculty.groups as ArrayList<Group>
        list.add(group)
        faculty.groups = list
        university.postValue(u)

    }*/

    /*fun newStudent(groupID: UUID, student: Student) {
        val u = university.value ?: return
        val faculty = u.find { it.groups.find { it.id == groupID } != null } ?: return
        val group = faculty.groups.find { it.id == groupID }
        val list: ArrayList<Student> = if (group!!.students.isEmpty())
            ArrayList()
        else
            group.students as ArrayList<Student>
        list.add(student)
        group.students = list
        university.postValue(u)
    }*/

    /*fun deleteStudent(groupID: UUID, student: Student) {
        val u = university.value ?: return
        val faculty = u.find { it.groups.find { it.id == groupID } != null } ?: return
        val group = faculty.groups.find { it.id == groupID }
        if (group!!.students.isEmpty()) return
        val list = group.students as ArrayList<Student>
        list.remove(student)
        group.students = list
        university.postValue(u)
    }*/

    /*fun editStudent(groupID: UUID, student: Student) {
        val u = university.value ?: return
        val faculty = u.find { it.groups.find { it.id == groupID } != null } ?: return
        val group = faculty.groups.find { it.id == groupID } ?: return
        val _student = group.students.find { it.id == student.id }
        if (_student == null) {
            newStudent(groupID, student)
            return
        }
        val list = group.students as ArrayList<Student>
        val i = list.indexOf(_student)
        list.remove(_student)
        list.add(i, student)
        group.students = list
        university.postValue(u)
    }*/
}