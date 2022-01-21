package com.rekover.robotfactory.infrastructure.db

import com.rekover.robotfactory.domain.Price
import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Component.A
import com.rekover.robotfactory.domain.model.Component.B
import com.rekover.robotfactory.domain.model.Component.C
import com.rekover.robotfactory.domain.model.Component.D
import com.rekover.robotfactory.domain.model.Component.E
import com.rekover.robotfactory.domain.model.Component.F
import com.rekover.robotfactory.domain.model.Component.G
import com.rekover.robotfactory.domain.model.Component.H
import com.rekover.robotfactory.domain.model.Component.I
import com.rekover.robotfactory.domain.model.Component.J
import com.rekover.robotfactory.domain.model.Error
import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class InMemoryStockRepositoryTest {

    private val stock = InMemoryStockRepository()


    @ParameterizedTest(name = "Component {0} cost {1} and remains {2} unit")
    @MethodSource("getData")
    fun `when we have stock for a component, decrease its stock and return its price`(
        component: Component,
        price: BigDecimal,
        remaining: Int) {

        val result = stock.getPart(component)

        assertThat(result).isEqualTo(Price(value = price))
        assertThat(stock.getAvailableUnitsOf(component)).isEqualTo(remaining)
    }

    @Test
    fun `when we don't have stock for a component, return a no stock error`() {

        assertThrows(
            Error.NoStockError::class.java,
            { stock.getPart(C) },
            "There are no stock of 'Steampunk Face' component")
    }

    companion object {
        @JvmStatic
        fun getData(): List<Arguments> =
            listOf(
                Arguments.of(A, 10.28.toBigDecimal(), 8),
                Arguments.of(B, 24.07.toBigDecimal(), 6),
                Arguments.of(D, 28.94.toBigDecimal(), 0),
                Arguments.of(E, 12.39.toBigDecimal(), 2),
                Arguments.of(F, 30.77.toBigDecimal(), 1),
                Arguments.of(G, 55.13.toBigDecimal(), 14),
                Arguments.of(H, 50.00.toBigDecimal(), 6),
                Arguments.of(I, 90.12.toBigDecimal(), 91),
                Arguments.of(J, 82.31.toBigDecimal(), 14),
            )
    }
}