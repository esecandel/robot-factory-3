package com.rekover.robotfactory.domain.model

import com.rekover.robotfactory.domain.ValidOrder

class Robot(private val order: ValidOrder) {

    init {
        validate()
    }

    private fun validate() {
        componentsIsNotAnEmptyList()
        componentsSizeIsOk()
        componentsAreOnlyOneOfEachPart()
    }

    private fun componentsAreOnlyOneOfEachPart() {
        if (order.components.none { it.isFace() }
            || order.components.none { it.isMaterial() }
            || order.components.none { it.isArms() }
            || order.components.none { it.isMobility() }) throw Error.WrongCombinationOfComponents
    }

    private fun componentsSizeIsOk() {
        if (order.components.size != 4) throw Error.NumberOfComponentsNotValid(order.components.size)
    }

    private fun componentsIsNotAnEmptyList() {
        if (order.components.isEmpty()) throw Error.EmptyListOfComponents
    }
}