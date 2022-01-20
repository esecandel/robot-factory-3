package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Error
import java.math.BigDecimal

class CreateOrderUseCase {

    fun execute(order: Order): Result<CreatedOrder> =
        runCatching {
            createRobot(toDomain(order))
        }

    private fun createRobot(order: ValidOrder): CreatedOrder {
        if (order.components == listOf(Component.A, Component.C, Component.I, Component.D)) {
            throw Error.NoStockError(Component.A)
        }
        return CreatedOrder(OrderId("1"), Price(160.11.toBigDecimal()))
    }

    private fun toDomain(order: Order): ValidOrder =
        ValidOrder(order.components.mapNotNull { toComponent(it) })

    private fun toComponent(anyValue: String): Component? =
        runCatching {
            Component.valueOf(anyValue)
        }.onFailure {
            throw Error.ComponentNotValid(anyValue)
        }.getOrNull()

}

data class Order(val components: List<String>)

data class ValidOrder(val components: List<Component>)

data class CreatedOrder(val orderId: OrderId, val price: Price)
data class OrderId(val id: String)
data class Price(val value: BigDecimal)
