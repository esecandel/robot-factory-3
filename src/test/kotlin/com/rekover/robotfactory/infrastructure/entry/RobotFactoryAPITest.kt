package com.rekover.robotfactory.infrastructure.entry

import com.rekover.robotfactory.domain.CreateOrderUseCase
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class RobotFactoryAPITest {

    private val createOrderUseCase: CreateOrderUseCase = mockk()
    private val controller = RobotFactoryAPI(createOrderUseCase)

    @Test
    fun `create an order successfully`() {
        val request = OrderRequest(listOf("I", "A", "D", "F"))

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(result.body).isEqualTo(OrderResponse(orderId = "1", total = 160.11f))
    }

    @Test
    fun `component not valid, return Bad Request status code`() {
        val request = OrderRequest(listOf("BENDER"))

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(result.body).isNull()
    }

    @Test
    fun `order without stock, return Unprocessable entity status code`() {
        val request = OrderRequest(listOf("A", "C", "I", "D"))

        val result = controller.createAnOrder(request)

        assertThat(result.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
        assertThat(result.body).isNull()
    }
}