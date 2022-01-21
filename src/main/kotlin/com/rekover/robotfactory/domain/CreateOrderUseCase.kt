package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

class CreateOrderUseCase(private val stockRepository: StockRepository,
                         private val orderRepository: OrderRepository) {

    fun execute(order: Order): CreatedOrder = createRobot(order.toDomain())

    private fun createRobot(order: ValidOrder): CreatedOrder {
        val savedOrder: SavedOrder = saveOrder(order)
        return toCreatedOrder(savedOrder)
    }

    private fun toCreatedOrder(savedOrder: SavedOrder) = CreatedOrder(
        orderId = savedOrder.orderId,
        price = savedOrder.price
    )

    private fun saveOrder(order: ValidOrder) = orderRepository.save(toNewOrder(order))

    private fun toNewOrder(order: ValidOrder) = NewOrder(
        price = Price(value = sumParts(order)),
        components = order.components
    )

    private fun sumParts(order: ValidOrder): BigDecimal = order.components.sumOf { stockRepository.getPart(it).value }

}

data class Order(val components: List<String>) {

    fun toDomain(): ValidOrder = ValidOrder(components.map { toComponent(it) })

    private fun toComponent(anyValue: String): Component =
        runCatching {
            Component.valueOf(anyValue)
        }.getOrElse { throw Error.ComponentNotValid(anyValue) }
}
