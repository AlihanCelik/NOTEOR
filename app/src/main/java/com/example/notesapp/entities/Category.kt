package com.example.notesapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category (
    @PrimaryKey(autoGenerate = true)
    var id_category: Int? = null,

    @ColumnInfo(name = "name_category")
    var name_category: String? = null

)