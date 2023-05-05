package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Faculty
import com.example.myapplication.data.Group
import com.example.myapplication.data.Student
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

const val SHARED_PREFERENCES_NAME = "UniversityAppPrefs"

class AppRepository private constructor() {
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()

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

    fun saveData(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val jsonUniversity = Gson().toJson(university.value)
        sharedPreferences.edit().putString(SHARED_PREFERENCES_NAME, jsonUniversity).apply()
    }

    fun loadData(context: Context) {
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
    }

    /*fun newFaculty(name: String) {
        val faculty = Faculty(name = name)
        val list: ArrayList<Faculty> =
            if (university.value != null) {
                (university.value as ArrayList<Faculty>)
            } else
                ArrayList()
        list.add(faculty)
        university.postValue(list)//оповещение всех пользователей
    }*/

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