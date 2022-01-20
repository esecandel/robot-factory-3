package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Error
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("when try to create an order")
internal class CreateOrderUseCaseTest {

    private val useCase = CreateOrderUseCase()

    @DisplayName("and there are no errors")
    @Nested
    inner class NoErrors {

        @Test
        fun `then return created order`() {
            val order = Order(listOf("I", "A", "D", "F"))

            val result = useCase.execute(order)
            val expected = CreatedOrder(orderId = OrderId("1"), price = Price(160.11.toBigDecimal()))

            assertTrue(result.isSuccess)
            assertThat(result.getOrNull()).isEqualTo(expected)
        }
    }

    @DisplayName("and there error is")
    @Nested
    inner class WithErrors {

        @Test
        fun `a not valid component, return a no valid component failure `() {
            val order = Order(listOf("BENDER"))

            val result = useCase.execute(order)

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertThat(exception).isInstanceOf(Error.ComponentNotValid::class.java)
            assertThat((exception as Error).message).isEqualTo("The component 'BENDER' is not valid")

        }

        @Test
        fun `no stock of a component, return a no stock component failure `() {
            val order = Order(listOf("A", "C", "I", "D"))

            val result = useCase.execute(order)

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertThat(exception).isInstanceOf(Error.NoStockError::class.java)
            assertThat((exception as Error).message).isEqualTo("There are no stock of 'Humanoid Face' component")

        }
    }

}