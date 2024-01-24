package com.example.notesapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val refreshList = MutableLiveData<Boolean>()
}