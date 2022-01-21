package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component.A
import com.rekover.robotfactory.domain.model.Component.D
import com.rekover.robotfactory.domain.model.Component.F
import com.rekover.robotfactory.domain.model.Component.I
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import com.rekover.robotfactory.domain.model.OrderId
import com.rekover.robotfactory.domain.model.Price
import com.rekover.robotfactory.domain.model.ValidOrder
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TransactionalRobotActionsTest {
    private val stockRepository: StockRepository = mockk()
    private val actions = TransactionalRobotActions(stockRepository)

    @DisplayName("and there are no errors")
    @Nested
    inner class NoErrors {

        @Test
        fun `then return created order with generated id`() {
            every { stockRepository.getPart(I) } returns Price(value = 90.12.toBigDecimal())
            every { stockRepository.getPart(A) } returns Price(value = 10.28.toBigDecimal())
            every { stockRepository.getPart(D) } returns Price(value = 28.94.toBigDecimal())
            every { stockRepository.getPart(F) } returns Price(value = 30.77.toBigDecimal())
            val order = ValidOrder(listOf(I, A, D, F))

            val result = actions.createRobot(order)
            val expected = CreatedOrder(orderId = OrderId("1"), price = Price(160.11.toBigDecimal()))

            Assertions.assertThat(result).isEqualTo(expected)
        }
    }

    @DisplayName("and there is an error")
    @Nested
    inner class AnyError {

        @Test
        fun `then return stock and don't generate id`() {
            every { stockRepository.getPart(I) } returns Price(value = 90.12.toBigDecimal())
            every { stockRepository.getPart(A) } returns Price(value = 10.28.toBigDecimal())
            every { stockRepository.getPart(D) } throws Error.NoStockError(D)
            every { stockRepository.returnPart(I) } just Runs
            every { stockRepository.returnPart(A) } just Runs

            val order = ValidOrder(listOf(I, A, D, F))

            assertThrows(
                Error.NoStockError::class.java,
                { actions.createRobot(order) },
                "There are no stock of 'Humanoid Face' component")

            verify { stockRepository.returnPart(I) }
            verify { stockRepository.returnPart(A) }
        }
    }
}