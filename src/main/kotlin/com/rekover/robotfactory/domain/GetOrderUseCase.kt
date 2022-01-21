package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.OrderId

class GetOrderUseCase(private val orderRepository: OrderRepository) {

    fun getOrder(orderId: OrderId): CreatedOrder {
        val savedOrder: SavedOrder = orderRepository.get(orderId)
        return toCreatedOrder(savedOrder)
    }

    private fun toCreatedOrder(savedOrder: SavedOrder) = CreatedOrder(
        orderId = savedOrder.orderId,
        price = savedOrder.price
    )
}