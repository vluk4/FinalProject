package domain.interactor

import domain.model.ChatMessage
import domain.model.User
import domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow

class MainInteractorImpl(
    private val repository: MainRepository
) : MainInteractor {
    override suspend fun saveUserDate(
        isOnline: Boolean,
        name: String,
        nickname: String,
        latitude: String,
        longitude: String,
        radius: String
    ): SaveUserDataResults {
        return runCatching {
            val userData = User(
                isOnline = isOnline,
                name = name,
                nickname = nickname,
                searchRadius = radius.toDouble(),
                latitude = latitude.toDouble(),
                longitude = longitude.toDouble()
            )
            repository.updateUserData(userData)

            SaveUserDataResults.SuccessfullySavedData(userData)
        }.getOrElse {
            println(it.printStackTrace())
            SaveUserDataResults.FailedToSavedData
        }
    }

    override suspend fun getUserData(): Flow<List<User>?> {
        return repository.listenToContacts()
    }

    override suspend fun initializeApplication(): InitializeFeatureResults {
        return runCatching {
            repository.initializeServices()
            InitializeFeatureResults.SuccessfullyInitialized
        }.getOrElse {
            InitializeFeatureResults.FailedToInitialize
        }
    }

    override fun shouldEnableSaveButton(
        name: String,
        nickname: String,
        radius: String,
        latitude: String,
        longitude: String
    ): Boolean {
        return name.isNotBlank() &&
                nickname.isNotBlank() &&
                radius.isNotBlank() &&
                latitude.isNotBlank() &&
                longitude.isNotBlank()
    }

    override suspend fun sendMessageToGeneralTopic(message: String, sender: String, receiver: String) {
        repository.sendMessageToGeneralTopic(
            message = ChatMessage(
                sender = sender,
                receiver = receiver,
                message = message
            )
        )
    }

    override suspend fun subscribeToGeneralTopic(): Flow<MutableMap<String, MutableList<ChatMessage>>> {
        return repository.subscribeToGeneralTopic()
    }
}