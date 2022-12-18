package domain.repository

import domain.model.User
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun updateUserData(user: User)
    suspend fun listenToContacts(): Flow<List<User>?>
}