package com.rekover.robotfactory.infrastructure.entry

import com.rekover.robotfactory.domain.CreateOrderUseCase
import com.rekover.robotfactory.domain.GetOrderUseCase
import com.rekover.robotfactory.domain.Order
import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class RobotFactoryAPITest {

    private val getOrderUseCase: GetOrderUseCase = mockk()
    private val createOrderUseCase: CreateOrderUseCase = mockk()
    private val controller = RobotFactoryAPI(createOrderUseCase, getOrderUseCase)

    @DisplayName("When try to create an order")
    @Nested
    inner class CreateOrder {
        @Test
        fun `and there no errors, create an order successfully`() {
            val request = OrderRequest(ANY_VALID_COMPONENTS)

            every {
                createOrderUseCase.execute(Order(ANY_VALID_COMPONENTS))
            } returns CreatedOrder(orderId = ANY_ORDER_ID, price = ANY_PRICE)

            val result = controller.createAnOrder(request)
            val expected = OrderResponse(orderId = ANY_ORDER_ID_VALUE, total = ANY_PRICE_VALUE)

            assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
            assertThat(result.body).isEqualTo(expected)
        }

        @Test
        fun `but the component is not valid, return Bad Request status code`() {
            val request = OrderRequest(listOf("BENDER"))

            every {
                createOrderUseCase.execute(Order(listOf("BENDER")))
            } throws Error.ComponentNotValid("error")

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(result.body).isNull()
        }

        @Test
        fun `but the number of component is not valid, return Bad Request status code`() {
            val request = OrderRequest(listOf("A", "C"))

            every {
                createOrderUseCase.execute(Order(listOf("A", "C")))
            } throws Error.NumberOfComponentsNotValid(2)

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(result.body).isNull()
        }

        @Test
        fun `but an empty list of components, return Bad Request status code`() {
            val request = OrderRequest(emptyList())

            every {
                createOrderUseCase.execute(Order(emptyList()))
            } throws Error.EmptyListOfComponents

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(result.body).isNull()
        }

        @Test
        fun `but an wrong combination of components, return Bad Request status code`() {
            val request = OrderRequest(listOf("A", "A", "A", "A"))

            every {
                createOrderUseCase.execute(Order(listOf("A", "A", "A", "A")))
            } throws Error.WrongCombinationOfComponents

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(result.body).isNull()
        }

        @Test
        fun `but the order has no stock, return Unprocessable entity status code`() {
            val request = OrderRequest(listOf("A", "C", "I", "D"))

            every {
                createOrderUseCase.execute(Order(listOf("A", "C", "I", "D")))
            } throws Error.NoStockError(Component.A)

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
            assertThat(result.body).isNull()
        }


        @Test
        fun `but an expected error, return internal server error status code`() {
            val request = OrderRequest(listOf("ERROR"))

            every {
                createOrderUseCase.execute(Order(listOf("ERROR")))
            } throws RuntimeException("error")

            val result = controller.createAnOrder(request)

            assertThat(result.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            assertThat(result.body).isNull()
        }
    }


    @DisplayName("When try to get a saved order")
    @Nested
    inner class GetOrder {

        @Test
        fun `and there no errors, return saved order successfully`() {

            every {
                getOrderUseCase.execute(ANY_ORDER_ID)
            } returns CreatedOrder(orderId = ANY_ORDER_ID, price = ANY_PRICE)

            val result = controller.getOrder(ANY_ORDER_ID_VALUE)
            val expected = OrderResponse(orderId = ANY_ORDER_ID_VALUE, total = ANY_PRICE_VALUE)

            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(result.body).isEqualTo(expected)
        }

        @Test
        fun `but the is not found, return Not Found status code`() {
            every {
                getOrderUseCase.execute(ANY_ORDER_ID)
            } throws Error.OrderNotFound(ANY_ORDER_ID)

            val result = controller.getOrder(ANY_ORDER_ID_VALUE)

            assertThat(result.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            assertThat(result.body).isNull()
        }

    }
}

private const val ANY_ORDER_ID_VALUE = "1"
private const val ANY_PRICE_VALUE = 160.11f
private val ANY_ORDER_ID = OrderId(id = ANY_ORDER_ID_VALUE)
private val ANY_PRICE = Price(ANY_PRICE_VALUE.toBigDecimal())
private val ANY_VALID_COMPONENTS = listOf("J", "A", "D", "F")