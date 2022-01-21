package com.rekover.robotfactory.infrastructure.entry

import com.fasterxml.jackson.annotation.JsonProperty
import com.rekover.robotfactory.domain.CreateOrderUseCase

import com.rekover.robotfactory.domain.Order
import com.rekover.robotfactory.domain.model.CreatedOrder
import com.rekover.robotfactory.domain.model.Error
import java.net.URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RobotFactoryAPI(private val createOrderUseCase: CreateOrderUseCase) {

    @PostMapping("/orders")
    fun createAnOrder(@RequestBody request: OrderRequest): ResponseEntity<OrderResponse?> =
        runCatching {
            createOrderUseCase.execute(request.toOrder())
        }.fold(
            onSuccess = {
                ResponseEntity
                    .created(URI("http://localhost:0/orders/1"))
                    .body(toOrderResponse(it))
            },
            onFailure = handleThrowable()
        )

    private fun toOrderResponse(createdOrder: CreatedOrder) = OrderResponse(
        orderId = createdOrder.orderId.id,
        total = createdOrder.price.value.toFloat()
    )

    private fun handleThrowable(): (Throwable) -> ResponseEntity<OrderResponse?> = { throwable ->
        when (throwable.parseThrowable()) {
            is Error.ComponentNotValid -> ResponseEntity.badRequest().build()
            is Error.NoStockError -> ResponseEntity.unprocessableEntity().build()
            else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}

internal fun Throwable.parseThrowable(): Exception =
    when (this) {
        is Exception -> this
        else -> throw this
    }

data class OrderRequest(val components: List<String>) {
    fun toOrder() = Order(components = components)
}

data class OrderResponse(
    @JsonProperty("order_id")
    val orderId: String,
    val total: Float
)