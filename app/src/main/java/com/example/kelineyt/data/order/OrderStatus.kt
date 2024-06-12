package com.example.kelineyt.data.order

sealed class OrderStatus(val status: String) {

    object Ordered: OrderStatus("Заказ оформлен")
    object Canceled: OrderStatus("Заказ отменен")
    object Confirmed: OrderStatus("Заказ подтвержден")
    object Shipped: OrderStatus("Заказ доставляется")
    object Delivered: OrderStatus("Заказ доставлен")
    object Returned: OrderStatus("Returned")
}

fun getOrderStatus(status: String): OrderStatus {
    return when (status) {
        "Заказ оформлен" -> {
            OrderStatus.Ordered
        }
        "Заказ отменен" -> {
            OrderStatus.Canceled
        }
        "Заказ подтвержден" -> {
            OrderStatus.Confirmed
        }
        "Заказ доставляется" -> {
            OrderStatus.Shipped
        }
        "Заказ доставлен" -> {
            OrderStatus.Delivered
        }
        else -> OrderStatus.Returned
    }
}
