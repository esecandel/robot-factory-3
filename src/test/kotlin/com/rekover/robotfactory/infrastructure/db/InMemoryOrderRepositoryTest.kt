package com.rekover.robotfactory.infrastructure.db

import com.rekover.robotfactory.domain.NewOrder
import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class InMemoryOrderRepositoryTest {

    private val idGenerator: IdGenerator = mockk()
    private val orderRepository = InMemoryOrderRepository(idGenerator)

    @Test
    fun `save an order and get it successfully`() {
        every { idGenerator.generateId() } returns ANY_KEY
        val newOrder = NewOrder(price = ANY_PRICE, components = ANY_COMPONENTS)

        val savedOrder = orderRepository.save(newOrder)

        Assertions.assertThat(savedOrder.orderId.id).isEqualTo(ANY_KEY)
        Assertions.assertThat(savedOrder.price).isEqualTo(ANY_PRICE)
        Assertions.assertThat(savedOrder.components).isEqualTo(ANY_COMPONENTS)

        val sameSaveOrder = orderRepository.get(OrderId(id = ANY_KEY))
        Assertions.assertThat(sameSaveOrder).isEqualTo(savedOrder)

    }

    @Test
    fun `when try to get order, and it doesn't exist, then return a not found error`() {
        assertThrows(
            Error.OrderNotFound::class.java,
            { orderRepository.get(OrderId(id = "NOT_EXISTING_ORDER")) },
            "The order 'NOT_EXISTING_ORDER' doesn't exist.")
    }
}

private const val ANY_KEY = "::any_id::"
private val ANY_PRICE = Price(BigDecimal.TEN)
private val ANY_COMPONENTS = listOf(Component.A)