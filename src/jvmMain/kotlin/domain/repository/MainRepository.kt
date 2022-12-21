package domain.repository

import domain.model.ChatMessage
import domain.model.User
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun updateUserData(user: User)
    suspend fun listenToContacts(): Flow<List<User>?>
    suspend fun initializeServices()
    suspend fun subscribeToGeneralTopic(): Flow<MutableMap<String, MutableList<ChatMessage>>>
    suspend fun sendMessageToGeneralTopic(message: ChatMessage)

}