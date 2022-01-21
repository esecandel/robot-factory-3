package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.OrderId

class GetOrderUseCase(private val orderRepository: OrderRepository) {

    fun execute(orderId: OrderId): CreatedOrder =
        toCreatedOrder(orderRepository.get(orderId))

    private fun toCreatedOrder(savedOrder: SavedOrder) = CreatedOrder(
        orderId = savedOrder.orderId,
        price = savedOrder.price
    )
}