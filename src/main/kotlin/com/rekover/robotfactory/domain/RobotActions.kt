package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

interface RobotActions {
    fun createRobot(order: ValidOrder): NewOrder
}

class TransactionalRobotActions(private val stockRepository: StockRepository) : RobotActions {

    override fun createRobot(order: ValidOrder): NewOrder =
        NewOrder(
            components = order.components,
            price = totalPrice(order)
        )

    private fun totalPrice(order: ValidOrder): Price {
        val reservedComponents: MutableList<Component> = mutableListOf()
        return Price(value = order.components.sumOf { reservePart(it, reservedComponents) })
    }

    private fun reservePart(component: Component, reservedComponents: MutableList<Component>): BigDecimal {
        return try {
            stockRepository.getPart(component).value.also { reservedComponents.add(component) }
        } catch (exception: Exception) {
            reservedComponents.forEach { reserved -> stockRepository.returnPart(reserved) }
            throw exception
        }
    }

}