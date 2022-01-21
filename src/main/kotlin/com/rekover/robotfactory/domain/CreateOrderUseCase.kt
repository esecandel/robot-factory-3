package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

class CreateOrderUseCase(private val stockRepository: StockRepository) {

    fun execute(order: Order): CreatedOrder = createRobot(order.toDomain())

    private fun createRobot(order: ValidOrder): CreatedOrder =
        CreatedOrder(
            orderId = OrderId(id = "1"),
            price = Price(value = sumParts(order))
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
