package com.example.logggg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _orderItems = MutableLiveData<List<OrderItem>>(emptyList())
    val orderItems: LiveData<List<OrderItem>> = _orderItems

    fun addItemToOrder(item: Item, quantity: Int) {
        val currentItems = _orderItems.value.orEmpty()
        val newItem = OrderItem(item, quantity)
        _orderItems.value = currentItems + newItem
    }
}
