package com.rekover.robotfactory.domain.model

sealed class Error(val errorMessage: String) {
    data class NoStockError(val error: String) : Error(error)
    data class ComponentNotValid(val error: String) : Error(error)
}