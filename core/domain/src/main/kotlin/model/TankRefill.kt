package model

import java.util.UUID

data class TankRefill(
    val groupId: UUID,
    val start: Int,
    val end: Int,
    val cost: Double,
    val tanker: String
)
