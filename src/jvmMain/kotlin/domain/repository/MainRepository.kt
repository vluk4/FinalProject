package domain.repository

import domain.model.UserConfiguration

interface MainRepository {
    suspend fun saveUserDataOnJavaSpace(userConfiguration: UserConfiguration)
}