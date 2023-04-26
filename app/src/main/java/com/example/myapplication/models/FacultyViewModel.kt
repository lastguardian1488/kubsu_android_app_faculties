package com.example.myapplication.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Faculty
import com.example.myapplication.repository.AppRepository

class FacultyViewModel : ViewModel() {
    var university: MutableLiveData<List<Faculty>> = MutableLiveData()

    init {
        AppRepository.get().university.observeForever {
            university.postValue(it)
        }
    }
}