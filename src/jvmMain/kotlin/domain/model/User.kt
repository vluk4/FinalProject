package domain.model

import java.io.Serializable
import kotlin.math.pow
import kotlin.math.sqrt

data class User(
    val isOnline: Boolean = false,
    val name: String = "",
    val nickname: String = "",
    val searchRadius: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Serializable {

    fun isOnCommunicableRadius(outerLatitude: Double, outerLongitude: Double): Boolean {
        val distance = sqrt((outerLatitude - this.latitude).pow(2.0) + (outerLongitude - this.longitude).pow(2.0))
        return distance <= searchRadius
    }
}
