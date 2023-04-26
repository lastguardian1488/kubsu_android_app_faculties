package com.example.myapplication.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Faculty
import com.example.myapplication.repository.AppRepository
import java.util.UUID

class GroupViewModel : ViewModel() {
    val faculty: MutableLiveData<Faculty?> = MutableLiveData()

    private var facultyID: UUID? = null

    init {
        AppRepository.get().university.observeForever {
            faculty.postValue(it.find { faculty -> faculty.id == facultyID })
        }
    }

    fun setFacultyId(facultyID: UUID) {
        this.facultyID = facultyID
        faculty.postValue(AppRepository.get().university.value?.find { faculty -> faculty.id == facultyID })
    }
}