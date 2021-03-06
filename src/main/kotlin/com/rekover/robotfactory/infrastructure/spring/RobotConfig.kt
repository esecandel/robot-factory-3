package com.rekover.robotfactory.infrastructure.spring

import com.rekover.robotfactory.domain.CreateOrderUseCase
import com.rekover.robotfactory.domain.GetOrderUseCase
import com.rekover.robotfactory.domain.OrderRepository
import com.rekover.robotfactory.domain.RobotActions
import com.rekover.robotfactory.domain.StockRepository
import com.rekover.robotfactory.domain.TransactionalRobotActions
import com.rekover.robotfactory.infrastructure.db.IdGenerator
import com.rekover.robotfactory.infrastructure.db.InMemoryOrderRepository
import com.rekover.robotfactory.infrastructure.db.InMemoryStockRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RobotConfig {

    @Bean
    fun createOrderUseCase(robotActions: RobotActions,
                           orderRepository: OrderRepository) = CreateOrderUseCase(orderRepository, robotActions)

    @Bean
    fun getOrderUseCase(orderRepository: OrderRepository) = GetOrderUseCase(orderRepository)

    @Bean
    fun stockRepository(): StockRepository = InMemoryStockRepository()

    @Bean
    fun orderRepository(): OrderRepository = InMemoryOrderRepository(IdGenerator())

    @Bean
    fun robotActions(stockRepository: StockRepository): RobotActions = TransactionalRobotActions(stockRepository)
}