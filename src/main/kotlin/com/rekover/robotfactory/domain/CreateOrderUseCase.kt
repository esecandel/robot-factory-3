package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.ValidOrder

class CreateOrderUseCase(private val orderRepository: OrderRepository,
                         private val robotActions: RobotActions) {

    fun execute(order: Order): CreatedOrder = createRobot(order.toDomain())

    private fun createRobot(order: ValidOrder): CreatedOrder =
        toCreatedOrder(saveOrder(robotActions.createRobot(order)))

    private fun toCreatedOrder(savedOrder: SavedOrder) = CreatedOrder(
        orderId = savedOrder.orderId,
        price = savedOrder.price
    )

    private fun saveOrder(newOrder: NewOrder) = orderRepository.save(newOrder)

}

data class Order(val components: List<String>) {

    fun toDomain(): ValidOrder = ValidOrder(components = components.map { toComponent(it) })

    private fun toComponent(anyValue: String): Component =
        runCatching {
            Component.valueOf(anyValue)
        }.getOrElse { throw Error.ComponentNotValid(anyValue) }
}
