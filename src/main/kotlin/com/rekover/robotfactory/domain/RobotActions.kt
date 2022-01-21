package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import java.math.BigDecimal

interface RobotActions {
    fun createRobot(order: ValidOrder): CreatedOrder
}

class TransactionalRobotActions(private val stockRepository: StockRepository) : RobotActions {


    override fun createRobot(order: ValidOrder): CreatedOrder {
        var reservedComponents: MutableList<Component> = mutableListOf()
        var totalPrice: BigDecimal = BigDecimal.ZERO
        order.components.forEach {
            try {
                totalPrice += stockRepository.getPart(it).value
            } catch (ex: Exception) {

            }
        }
        return CreatedOrder(
            orderId = OrderId(id = "1"),
            price = Price(value = totalPrice)
        )

    }
}