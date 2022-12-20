package domain.repository

import domain.model.ChatMessage
import domain.model.User
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun updateUserData(user: User)
    suspend fun listenToContacts(): Flow<List<User>?>
    suspend fun initializeJavaSpace()
    suspend fun subscribeToChatTopic(sender: String, receiver: String): Flow<List<ChatMessage>?>
    suspend fun sendMessageToTopic(message: ChatMessage, receiver: User): MutableList<ChatMessage>?

}