package domain.model

import java.text.SimpleDateFormat
import java.util.Date

data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val date: Date = Date()
) {
    val hours: String
        get() {
            val formatter = SimpleDateFormat("mm:HH")
            return formatter.format(date)
        }
}
