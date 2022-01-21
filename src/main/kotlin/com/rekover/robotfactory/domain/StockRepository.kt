package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component

interface StockRepository {

    fun getPart(component: Component): Price

    fun getAvailableUnitsOf(component: Component): Int
}