package domain.interactor

interface MainInteractor {
    suspend fun saveUserDate(
        isOnline: Boolean,
        name: String,
        nickname: String,
        latitude: String,
        longitude: String
    ): SaveUserDataResults
}