package com.example.logggg

data class OrderItem(
    val item: Item,
    val quantity: Int
) {
    val totalCost: Double
        get() = item.price * quantity
}

