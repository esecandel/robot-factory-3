package com.rekover.robotfactory.domain.model

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ComponentTest {

    @Test
    fun isFace() {
        assertTrue(Component.A.isFace())
        assertTrue(Component.B.isFace())
        assertTrue(Component.C.isFace())
    }

    @Test
    fun isMaterial() {
        assertTrue(Component.I.isMaterial())
        assertTrue(Component.J.isMaterial())
    }

    @Test
    fun isArms() {
        assertTrue(Component.D.isArms())
        assertTrue(Component.E.isArms())
    }

    @Test
    fun isMobility() {
        assertTrue(Component.F.isMobility())
        assertTrue(Component.G.isMobility())
        assertTrue(Component.H.isMobility())
    }
}