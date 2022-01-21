package com.rekover.robotfactory.domain

import com.rekover.robotfactory.domain.model.Component
import com.rekover.robotfactory.domain.model.Price

interface StockRepository {
    fun getPart(component: Component): Price
    fun getAvailableUnitsOf(component: Component): Int
    fun returnPart(component: Component)
}