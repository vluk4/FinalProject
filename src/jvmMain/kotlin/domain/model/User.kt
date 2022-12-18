package domain.model

import java.io.Serializable

data class User(
    val isOnline: Boolean = false,
    val name: String = "",
    val nickname: String = "",
    val searchRadius: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Serializable
