package com.rekover.robotfactory.infrastructure.spring

import com.rekover.robotfactory.domain.CreateOrderUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RobotConfig {

    @Bean
    fun createOrderUseCase() = CreateOrderUseCase()
}