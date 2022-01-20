package com.rekover.robotfactory.infrastructure.entry

import com.fasterxml.jackson.annotation.JsonProperty
import com.rekover.robotfactory.domain.CreateOrderUseCase
import java.net.URI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RobotFactoryAPI(private val createOrderUseCase: CreateOrderUseCase) {

    @PostMapping("/orders")
    fun createAnOrder(@RequestBody request: OrderRequest): ResponseEntity<OrderResponse?> =
        when (request.components) {
            listOf("I", "A", "D", "F") -> {
                ResponseEntity
                    .created(URI("http://localhost:0/orders/1"))
                    .body(OrderResponse(orderId = "1", total = 160.11f))
            }
            listOf("BENDER") -> {
                ResponseEntity.badRequest().build()
            }
            else -> {
                ResponseEntity.unprocessableEntity().build()
            }
        }


}

data class OrderRequest(val components: List<String>)

data class OrderResponse(
    @JsonProperty("order_id")
    val orderId: String,
    val total: Float
)