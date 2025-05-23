package com.example.shoppinglist.data

import androidx.compose.ui.res.stringResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val title:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "price") val price:Int,
    @ColumnInfo(name = "category") var category:ShoppingCategory = ShoppingCategory.OTHER,
    @ColumnInfo(name = "isBought") var isBought: Boolean = false
) : Serializable

enum class ShoppingCategory {
    FOOD, ELECTRONICS, BOOK, OTHER;

    fun getIcon(): Int {
        return when (this) {
            FOOD -> R.drawable.food
            ELECTRONICS -> R.drawable.electronics
            BOOK -> R.drawable.book
            OTHER -> R.drawable.miscellaneous
        }
    }
}