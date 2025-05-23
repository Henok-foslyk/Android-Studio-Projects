package com.example.shoppinglist.ui.screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.shoppinglist.data.ShoppingDAO
import com.example.shoppinglist.data.ShoppingItem
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class ShoppingViewModel @Inject constructor(val shoppingDAO: ShoppingDAO) : ViewModel() {

    fun getAllItems(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllItems()
    }

    suspend fun getAllItemsNum(): Int {
        return shoppingDAO.getItemsNum()
    }

    suspend fun getFoodsNum(): Int {
        return shoppingDAO.getFoodItemsNum()
    }

    suspend fun getBooksNum(): Int {
        return shoppingDAO.getBookItemsNum()
    }

    suspend fun getElectronicsNum(): Int {
        return shoppingDAO.getElectronicItemsNum()
    }

    fun addShoppingList(shoppingItem: ShoppingItem) {
        // launch: launch a new coroutine in the scope of the current ViewModel
        viewModelScope.launch() {
            shoppingDAO.insert(shoppingItem)
        }
    }

    fun removeShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.delete(shoppingItem)
        }
    }

    fun editShoppingItem(editedShopping: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.update(editedShopping)
        }
    }

    fun changeShoppingState(shoppingItem: ShoppingItem, value: Boolean) {
        // because copy makes a new instance,
        // this will trigger the state change in the table
        val updatedShopping = shoppingItem.copy()
        updatedShopping.isBought = value
        viewModelScope.launch {
            shoppingDAO.update(updatedShopping)
        }
    }

    fun clearAllItems() {
        viewModelScope.launch {
            shoppingDAO.deleteAllItems()
        }
    }
}