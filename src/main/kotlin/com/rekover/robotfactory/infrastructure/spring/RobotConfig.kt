package com.rekover.robotfactory.infrastructure.spring

import com.rekover.robotfactory.domain.CreateOrderUseCase
import com.rekover.robotfactory.domain.GetOrderUseCase
import com.rekover.robotfactory.domain.OrderRepository
import com.rekover.robotfactory.domain.StockRepository
import com.rekover.robotfactory.infrastructure.db.IdGenerator
import com.rekover.robotfactory.infrastructure.db.InMemoryOrderRepository
import com.rekover.robotfactory.infrastructure.db.InMemoryStockRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RobotConfig {

    @Bean
    fun createOrderUseCase(stockRepository: StockRepository,
                           orderRepository: OrderRepository) = CreateOrderUseCase(stockRepository, orderRepository)

    @Bean
    fun getOrderUseCase(orderRepository: OrderRepository) = GetOrderUseCase(orderRepository)

    @Bean
    fun stockRepository(): StockRepository = InMemoryStockRepository()

    @Bean
    fun orderRepository(): OrderRepository = InMemoryOrderRepository(IdGenerator())
}