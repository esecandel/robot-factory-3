package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

class CreateOrderUseCase(private val stockRepository: StockRepository) {

    fun execute(order: Order): CreatedOrder = createRobot(toDomain(order))

    private fun createRobot(order: ValidOrder): CreatedOrder =
        CreatedOrder(
            orderId = OrderId(id = "1"),
            price = Price(
                value = order.components.sumOf { stockRepository.getPart(it).value }
            )
        )

    private fun toDomain(order: Order): ValidOrder = ValidOrder(order.components.map { toComponent(it) })

    private fun toComponent(anyValue: String): Component =
        runCatching {
            Component.valueOf(anyValue)
        }.getOrElse { throw Error.ComponentNotValid(anyValue) }

}

data class Order(val components: List<String>)
data class CreatedOrder(val orderId: OrderId, val price: Price)
data class OrderId(val id: String)
data class Price(val value: BigDecimal)
