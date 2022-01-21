package com.rekover.robotfactory.infrastructure.db

import com.rekover.robotfactory.domain.Price
import com.rekover.robotfactory.domain.StockRepository
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

class InMemoryStockRepository : StockRepository {

    private val stock = mapOf(
        A to ComponentStock(price = Price(value = 10.28.toBigDecimal()), stock = Stock(available = 9)),
        B to ComponentStock(price = Price(value = 24.07.toBigDecimal()), stock = Stock(available = 7)),
        C to ComponentStock(price = Price(value = 13.30.toBigDecimal()), stock = Stock(available = 0)),
        D to ComponentStock(price = Price(value = 28.94.toBigDecimal()), stock = Stock(available = 1)),
        E to ComponentStock(price = Price(value = 12.39.toBigDecimal()), stock = Stock(available = 3)),
        F to ComponentStock(price = Price(value = 30.77.toBigDecimal()), stock = Stock(available = 2)),
        G to ComponentStock(price = Price(value = 55.13.toBigDecimal()), stock = Stock(available = 15)),
        H to ComponentStock(price = Price(value = 50.00.toBigDecimal()), stock = Stock(available = 7)),
        I to ComponentStock(price = Price(value = 90.12.toBigDecimal()), stock = Stock(available = 92)),
        J to ComponentStock(price = Price(value = 82.31.toBigDecimal()), stock = Stock(available = 15)),
    )

    override fun getPart(component: Component): Price =
        stock[component]?.price.also { decreaseStock(component) }
            ?: throw Error.NoStockError(component)

    private fun decreaseStock(component: Component) {
        if (stock[component]?.stock?.available == 0) {
            throw Error.NoStockError(component)
        }
        stock[component]?.stock?.decrease()
    }

    override fun getAvailableUnitsOf(component: Component): Int =
        stock[component]?.stock?.available ?: throw Error.NoStockError(component)

}

data class Stock(var available: Int) {
    fun decrease() {
        available -= 1
    }
}

data class ComponentStock(val price: Price, val stock: Stock)