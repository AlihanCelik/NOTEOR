package com.example.notesapp.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "sub_title")
    var subTitle: String? = null,

    @ColumnInfo(name = "date_time")
    var dateTime: String? = null,

    @ColumnInfo(name = "note_text")
    var noteText: String? = null,

    @ColumnInfo(name = "img_path")
    var imgPath: List<Uri>? = null,

    @ColumnInfo(name = "web_link")
    var webLink: List<String>? = null,

    @ColumnInfo(name = "favorite")
    var favorite: Boolean? = false,

    @ColumnInfo(name = "color")
    var color: String? = null


) : Serializable {
    override fun toString(): String {
        return "$title : $dateTime"
    }
}