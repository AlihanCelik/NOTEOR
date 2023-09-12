package com.example.notesapp.entities


import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Trash")
data class Trash(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "title_t")
    var title_t: String? = null,

    @ColumnInfo(name = "sub_title_T")
    var subTitle_t: String? = null,

    @ColumnInfo(name = "date_time_t")
    var dateTime_t: String? = null,

    @ColumnInfo(name = "note_text_t")
    var noteText_t: String? = null,

    @ColumnInfo(name = "img_path_t")
    var imgPath_t: List<Uri>? = null,

    @ColumnInfo(name = "web_link_t")
    var webLink_t: List<String>? = null,

    @ColumnInfo(name = "favorite_t")
    var favorite_t: Boolean? = false,

    @ColumnInfo(name = "color_t")
    var color_t: String? = null,

    @ColumnInfo(name = "password_t")
    var password_t: String? = null




) : Serializable {
    override fun toString(): String {
        return "$title_t : $dateTime_t"
    }
}