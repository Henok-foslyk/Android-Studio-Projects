package com.example.todolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.R
import java.io.Serializable

@Entity(tableName = "todotable")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "priority") var priority:TodoPriority,
    @ColumnInfo(name = "isDone") var isDone: Boolean
) : Serializable

enum class TodoPriority {
    NORMAL, HIGH;

    fun getIcon(): Int {
        return if (this == NORMAL) R.drawable.normal else R.drawable.important
    }
}