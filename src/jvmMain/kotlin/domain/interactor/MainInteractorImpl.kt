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

    override suspend fun subscribeToChat(sender: String, receiver: String): Flow<List<ChatMessage>?> {
        return repository.subscribeToChatTopic(sender, receiver)
    }

    override suspend fun sendAndGetMessages(message: String, sender: User, receiver: User): MutableList<ChatMessage>? {
        val chatMessage = ChatMessage(sender = sender.name, message = message)
        return repository.sendMessageToTopic(chatMessage, receiver)
    }
}