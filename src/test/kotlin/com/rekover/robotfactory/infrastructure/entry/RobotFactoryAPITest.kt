package com.rekover.robotfactory.infrastructure.entry

import com.rekover.robotfactory.domain.CreateOrderUseCase
import com.rekover.robotfactory.domain.Order
import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class RobotFactoryAPITest {

    private val createOrderUseCase: CreateOrderUseCase = mockk()
    private val controller = RobotFactoryAPI(createOrderUseCase)

    @Test
    fun `create an order successfully`() {
        val request = OrderRequest(ANY_VALID_COMPONENTS)
        val expected = OrderResponse(orderId = ANY_ORDER_ID_VALUE, total = ANY_PRICE_VALUE)

        every {
            createOrderUseCase.execute(Order(ANY_VALID_COMPONENTS))
        } returns CreatedOrder(orderId = ANY_ORDER_ID, price = ANY_PRICE)

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(result.body).isEqualTo(expected)
    }

    @Test
    fun `component not valid, return Bad Request status code`() {
        val request = OrderRequest(listOf("BENDER"))

        every {
            createOrderUseCase.execute(Order(listOf("BENDER")))
        } throws Error.ComponentNotValid("error")

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body).isNull()
    }

    @Test
    fun `order without stock, return Unprocessable entity status code`() {
        val request = OrderRequest(listOf("A", "C", "I", "D"))

        every {
            createOrderUseCase.execute(Order(listOf("A", "C", "I", "D")))
        } throws Error.NoStockError(Component.A)

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(result.body).isNull()
    }


    @Test
    fun `un expected error, return internal server error status code`() {
        val request = OrderRequest(listOf("ERROR"))

        every {
            createOrderUseCase.execute(Order(listOf("ERROR")))
        } throws RuntimeException("error")

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        assertThat(result.body).isNull()
    }
}

private const val ANY_ORDER_ID_VALUE = "1"
private const val ANY_PRICE_VALUE = 160.11f
private val ANY_ORDER_ID = OrderId(id = ANY_ORDER_ID_VALUE)
private val ANY_PRICE = Price(ANY_PRICE_VALUE.toBigDecimal())
private val ANY_VALID_COMPONENTS = listOf("J", "A", "D", "F")