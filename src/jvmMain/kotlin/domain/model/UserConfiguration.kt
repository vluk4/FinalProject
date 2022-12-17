package domain.model

import java.io.Serializable

data class UserConfiguration(
    val isOnline: Boolean,
    val name: String,
    val nickname: String,
    val latitude: Double,
    val longitude: Double
): Serializable
