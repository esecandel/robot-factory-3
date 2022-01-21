package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class GetOrderUseCaseTest {

    private val orderRepository: OrderRepository = mockk()
    private val useCase = GetOrderUseCase(orderRepository)

    @Test
    fun `when try to get an order and it exists, then return it`() {
        every { orderRepository.get(ANY_ORDER_ID) } returns ANY_SAVED_ORDER

        val result = useCase.execute(ANY_ORDER_ID)

        Assertions.assertThat(result).isEqualTo(
            CreatedOrder(
                orderId = ANY_ORDER_ID,
                price = ANY_PRICE
            ))
    }

    @Test
    fun `when try to get an order but it doesn't exists, then return a not found error`() {
        every { orderRepository.get(ANY_ORDER_ID) } throws Error.OrderNotFound(ANY_ORDER_ID)

        org.junit.jupiter.api.Assertions.assertThrows(
            Error.OrderNotFound::class.java,
            { useCase.execute(ANY_ORDER_ID) },
            "The order '${ANY_ORDER_ID.id}' doesn't exist.")
    }

}

private val ANY_ORDER_ID = OrderId(id = "1")
private val ANY_PRICE = Price(160.11.toBigDecimal())
private val ANY_SAVED_ORDER = SavedOrder(
    price = ANY_PRICE,
    components = listOf(Component.I, Component.A, Component.D, Component.F),
    orderId = ANY_ORDER_ID
)