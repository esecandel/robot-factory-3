package com.rekover.robotfactory.infrastructure.db

import com.rekover.robotfactory.domain.NewOrder
import com.rekover.robotfactory.domain.OrderRepository
import com.rekover.robotfactory.domain.SavedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId

class InMemoryOrderRepository(private val idGenerator: IdGenerator) : OrderRepository {

    private val orders = mutableMapOf<String, SavedOrder>()

    override fun save(newOrder: NewOrder): SavedOrder {
        val orderId = idGenerator.generateId()
        val savedOrder = toSavedOrder(orderId, newOrder)
        orders[orderId] = savedOrder
        return savedOrder
    }

    override fun get(orderId: OrderId): SavedOrder =
        orders[orderId.id] ?: throw Error.OrderNotFound(orderId)

    private fun toSavedOrder(orderId: String, newOrder: NewOrder) = SavedOrder(
        orderId = OrderId(id = orderId),
        components = newOrder.components,
        price = newOrder.price
    )


}

class IdGenerator {
    fun generateId(): String = kotlin.random.Random.nextInt(0, 10000).toString()
}