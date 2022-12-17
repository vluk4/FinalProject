package domain.interactor

import domain.model.UserConfiguration
import domain.repository.MainRepository

class MainInteractorImpl(
    private val repository: MainRepository
) : MainInteractor {
    override suspend fun saveUserDate(
        isOnline: Boolean,
        name: String,
        nickname: String,
        latitude: String,
        longitude: String
    ): SaveUserDataResults {
        return runCatching {
            val userData = UserConfiguration(
                isOnline = isOnline,
                name = name,
                nickname = nickname,
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble()
            )
            repository.saveUserDataOnJavaSpace(userData)

            SaveUserDataResults.SuccessfullySavedData
        }.getOrElse {
            println(it.printStackTrace())
            SaveUserDataResults.FailedToSavedData
        }
    }
}