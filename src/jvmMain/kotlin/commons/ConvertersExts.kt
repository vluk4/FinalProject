package commons

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import domain.model.ChatMessage
import domain.model.User

fun String.toChatMessage(): ChatMessage? {
    return runCatching {
        Gson().fromJson(this, ChatMessage::class.java)
    }.getOrElse {
        null
    }
}
fun String?.toUserList(): List<User> {
    val gson = Gson()
    val type = object : TypeToken<List<User>>() {}.type

    return runCatching {
        gson.fromJson<List<User>>(this, type)
    }.getOrElse {
        listOf()
    }
}