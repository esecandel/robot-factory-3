package com.rekover.robotfactory.infrastructure.spring

import com.rekover.robotfactory.domain.CreateOrderUseCase
import com.rekover.robotfactory.domain.StockRepository
import com.rekover.robotfactory.infrastructure.db.InMemoryStockRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RobotConfig {

    @Bean
    fun createOrderUseCase(stockRepository: StockRepository) = CreateOrderUseCase(stockRepository)

    @Bean
    fun stockRepository(): StockRepository = InMemoryStockRepository()
}