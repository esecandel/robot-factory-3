package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

interface RobotActions {
    fun createRobot(order: ValidOrder): NewOrder
}

class TransactionalRobotActions(private val stockRepository: StockRepository) : RobotActions {

    override fun createRobot(order: ValidOrder): NewOrder {
        val reservedComponents: MutableList<Component> = mutableListOf()
        var totalPrice: BigDecimal = BigDecimal.ZERO
        order.components.forEach {
            try {
                totalPrice += stockRepository.getPart(it).value
                reservedComponents.add(it)
            } catch (exception: Exception) {
                reservedComponents.forEach { reserved -> stockRepository.returnPart(reserved) }
                throw exception
            }
        }
        return NewOrder(
            components = order.components,
            price = Price(value = totalPrice)
        )
    }
}