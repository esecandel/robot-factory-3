package com.rekover.robotfactory.domain.model

import com.rekover.robotfactory.domain.model.Component.A
import com.rekover.robotfactory.domain.model.Component.D
import com.rekover.robotfactory.domain.model.Component.F
import com.rekover.robotfactory.domain.model.Component.I
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("when try to create a valid domain order")
internal class ValidOrderTest {

    @DisplayName("with a validation error ")
    @Nested
    inner class ValidationError {

        @Test
        fun `as an empty list of components, then throw an empty list of components error`() {
            assertThrows(
                Error.EmptyListOfComponents::class.java,
                { ValidOrder(components = emptyList()) },
                "The list of components is empty. The robot cannot be built."
            )
        }

        @Test
        fun `as more than 4 list of components, then throw an wrong number of components error`() {
            assertThrows(
                Error.NumberOfComponentsNotValid::class.java,
                { ValidOrder(components = listOf(A, D, F, I, I)) },
                "The number of components must be 4, but it's 5"
            )
        }

        @Test
        fun `as less than 4 list of components, then throw an wrong number of components error`() {
            assertThrows(
                Error.NumberOfComponentsNotValid::class.java,
                { ValidOrder(components = listOf(A, D, F)) },
                "The number of components must be 4, but it's 3"
            )
        }


        @Test
        fun `as wrong combination of components, then throw a wrong combination of components error`() {
            assertThrows(
                Error.WrongCombinationOfComponents::class.java,
                { ValidOrder(components = listOf(A, D, F, F)) },
                "The number of components must be 4, but it's 3"
            )
        }
    }
}