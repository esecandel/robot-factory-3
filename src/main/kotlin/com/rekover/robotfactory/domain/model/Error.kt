package com.rekover.robotfactory.domain.model

sealed class Error(errorMessage: String) : Throwable(errorMessage) {
    data class NoStockError(val component: Component) : Error("There are no stock of '${component.part}' component")
    data class ComponentNotValid(val component: String) : Error("The component '$component' is not valid")
}