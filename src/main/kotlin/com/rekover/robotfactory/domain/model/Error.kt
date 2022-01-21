package com.rekover.robotfactory.domain.model

sealed class Error(errorMessage: String) : Exception(errorMessage) {
    data class NoStockError(val component: Component) : Error("There are no stock of '${component.part}' component")
    data class ComponentNotValid(val component: String) : Error("The component '$component' is not valid")
    data class NumberOfComponentsNotValid(val size: Int) : Error("The number of components must be 4, but it's $size")
    data class OrderNotFound(val orderId: OrderId) : Error("The order '${orderId.id}' doesn't exist.")
    object EmptyListOfComponents : Error("The list of components is empty. The robot cannot be built.")
    object WrongCombinationOfComponents : Error("The list of components must contains contains one, and only one, part of face, material, arms and mobility")
}