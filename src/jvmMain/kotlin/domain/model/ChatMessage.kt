package domain.model

import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import java.text.SimpleDateFormat
import java.util.Date

data class ChatMessage(
    val sender: String = "",
    val receiver: String = "",
    val message: String = "",
    val date: Date = Date()
) {
    val hours: String
        get() {
            val formatter = SimpleDateFormat("HH:mm")
            return formatter.format(date)
        }
}
