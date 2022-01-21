package com.rekover.robotfactory.domain.model

import com.rekover.robotfactory.domain.model.ComponentType.ARMS
import com.rekover.robotfactory.domain.model.ComponentType.FACE
import com.rekover.robotfactory.domain.model.ComponentType.MATERIAL
import com.rekover.robotfactory.domain.model.ComponentType.MOBILITY
import java.math.BigDecimal

enum class Component(val part: String, private val type: ComponentType) {

    A("Humanoid Face", FACE),
    B("LCD Face", FACE),
    C("Steampunk Face", FACE),
    D("Arms with Hands", ARMS),
    E("Arms with Grippers", ARMS),
    F("Mobility with Wheels", MOBILITY),
    G("Mobility with Legs", MOBILITY),
    H("Mobility with Tracks", MOBILITY),
    I("Material Bioplastic", MATERIAL),
    J("Material Metallic", MATERIAL), ;

    fun isFace(): Boolean = this.type == FACE
    fun isMaterial(): Boolean = this.type == MATERIAL
    fun isArms(): Boolean = this.type == ARMS
    fun isMobility(): Boolean = this.type == MOBILITY

}

enum class ComponentType {
    FACE, MATERIAL, ARMS, MOBILITY
}

data class OrderId(val id: String)
data class Price(val value: BigDecimal)