package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price

interface OrderRepository {

    fun save(newOrder: NewOrder): SavedOrder
    fun get(orderId: OrderId): SavedOrder
}

data class NewOrder(val price: Price, val components: List<Component>)
data class SavedOrder(val price: Price, val components: List<Component>, val orderId: OrderId)