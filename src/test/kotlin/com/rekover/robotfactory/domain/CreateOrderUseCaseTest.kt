package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component.A
import com.rekover.robotfactory.domain.model.Component.C
import com.rekover.robotfactory.domain.model.Component.D
import com.rekover.robotfactory.domain.model.Component.F
import com.rekover.robotfactory.domain.model.Component.I
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("when try to create an order")
internal class CreateOrderUseCaseTest {
    private val orderRepository: OrderRepository = mockk()
    private val stockRepository: StockRepository = mockk()
    private val useCase = CreateOrderUseCase(stockRepository, orderRepository)

    @DisplayName("and there are no errors")
    @Nested
    inner class NoErrors {

        @Test
        fun `then return created order`() {
            every { stockRepository.getPart(I) } returns Price(value = 90.12.toBigDecimal())
            every { stockRepository.getPart(A) } returns Price(value = 10.28.toBigDecimal())
            every { stockRepository.getPart(D) } returns Price(value = 28.94.toBigDecimal())
            every { stockRepository.getPart(F) } returns Price(value = 30.77.toBigDecimal())
            every {
                orderRepository.save(
                    NewOrder(
                        price = Price(160.11.toBigDecimal()),
                        components = listOf(I, A, D, F)
                    )
                )
            } returns SavedOrder(
                price = Price(160.11.toBigDecimal()),
                components = listOf(I, A, D, F),
                orderId = OrderId("1")
            )

            val order = Order(listOf("I", "A", "D", "F"))

            val result = useCase.execute(order)
            val expected = CreatedOrder(orderId = OrderId("1"), price = Price(160.11.toBigDecimal()))

            assertThat(result).isEqualTo(expected)
        }
    }

    @DisplayName("and there error is")
    @Nested
    inner class WithErrors {

        @Test
        fun `a not valid component, return a no valid component failure `() {
            val order = Order(listOf("BENDER"))

            assertThrows(Error.ComponentNotValid::class.java, { useCase.execute(order) }, "The component 'BENDER' is not valid")
        }

        @Test
        fun `no stock of a component, return a no stock component failure `() {
            every { stockRepository.getPart(C) } throws Error.NoStockError(C)

            val order = Order(listOf("C", "I", "D", "F"))

            assertThrows(Error.NoStockError::class.java, { useCase.execute(order) }, "There are no stock of 'Humanoid Face' component")

        }
    }

}