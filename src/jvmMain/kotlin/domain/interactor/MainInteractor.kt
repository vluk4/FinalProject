package domain.interactor

import domain.model.User
import kotlinx.coroutines.flow.Flow

interface MainInteractor {
    suspend fun saveUserDate(
        isOnline: Boolean,
        name: String,
        nickname: String,
        latitude: String,
        longitude: String,
        radius: String
    ): SaveUserDataResults

    suspend fun getUserData(): Flow<List<User>?>
    suspend fun initializeApplication(): InitializeFeatureResults
    fun shouldEnableSaveButton(name: String, nickname: String, radius: String, latitude: String, longitude: String): Boolean
}