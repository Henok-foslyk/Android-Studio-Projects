package com.example.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDAO {
    @Query("SELECT * FROM shoppingtable")
    fun getAllItems() : Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingtable WHERE id = :id")
    fun getItem(id: Int): Flow<ShoppingItem>

    // counts how many row we have in the table
    @Query("SELECT COUNT(*) from shoppingtable")
    suspend fun getItemsNum(): Int

    @Query("""SELECT COUNT(*) from shoppingtable WHERE category="FOOD"""")
    suspend fun getFoodItemsNum(): Int

    @Query("""SELECT COUNT(*) from shoppingtable WHERE category="BOOK"""")
    suspend fun getBookItemsNum(): Int

    @Query("""SELECT COUNT(*) from shoppingtable WHERE category="ELECTRONICS"""")
    suspend fun getElectronicItemsNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE from shoppingtable")
    suspend fun deleteAllItems()
}