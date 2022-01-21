package com.rekover.robotfactory.domain.model

data class ValidOrder(val components: List<Component>) {

    init {
        validate()
    }

    private fun validate() {
        componentsIsNotAnEmptyList()
        componentsSizeIsOk()
        componentsAreOnlyOneOfEachPart()
    }

    private fun componentsAreOnlyOneOfEachPart() {
        if (components.none { it.isFace() }
            || components.none { it.isMaterial() }
            || components.none { it.isArms() }
            || components.none { it.isMobility() }) throw Error.WrongCombinationOfComponents
    }

    private fun componentsSizeIsOk() {
        if (components.size != 4) throw Error.NumberOfComponentsNotValid(components.size)
    }

    private fun componentsIsNotAnEmptyList() {
        if (components.isEmpty()) throw Error.EmptyListOfComponents
    }
}