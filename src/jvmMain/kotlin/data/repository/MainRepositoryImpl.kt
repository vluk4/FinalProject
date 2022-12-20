package data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import commons.toChatMessage
import commons.toUserList
import data.services.mom.Publisher
import data.services.mom.Subscriber
import data.services.tuples.TuplesSpace
import domain.model.ChatMessage
import domain.model.User
import domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MainRepositoryImpl(private val javaSpace: TuplesSpace) : MainRepository {

    private lateinit var subscriber: Subscriber
    private val clientMessages: MutableMap<String, MutableList<ChatMessage>> = mutableMapOf()
    override suspend fun initializeJavaSpace() = withContext(Dispatchers.IO) {
        javaSpace.init()
    }

    override suspend fun subscribeToChatTopic(sender: String, receiver: String): Flow<List<ChatMessage>?> =
        withContext(Dispatchers.IO) {
            subscriber = Subscriber(receiver)
            var oldMessage: ChatMessage? = null
            flow {
                while (true) {
                    delay(1000)
                    val receivedMessage = subscriber.mensagemRecebida.toChatMessage()

                    if (receivedMessage != oldMessage) {
                        receivedMessage?.let {
                            clientMessages.getOrPut(receiver) {
                                mutableListOf(receivedMessage)
                            }.add(it)

                            emit(clientMessages[receiver])
                            oldMessage = receivedMessage
                        }
                    }
                }
            }
        }

    override suspend fun sendMessageToTopic(message: ChatMessage, receiver: User): MutableList<ChatMessage>? =
        withContext(Dispatchers.IO) {
            val messageJson = Gson().toJson(message)
            Publisher(message.sender, messageJson)
            clientMessages.getOrPut(receiver.name) { mutableListOf(message) }.add(message)

            return@withContext clientMessages[receiver.name]
        }

    override suspend fun updateUserData(user: User) =
        withContext(Dispatchers.IO) {
            val gson = Gson()
            val type = object : TypeToken<List<User>>() {}.type
            val userListTuple = javaSpace.take(USER_LIST_IDENTIFIER)
            if (!userListTuple.isNullOrEmpty()) {
                val users = gson.fromJson<List<User>>(userListTuple, type).toMutableList()

                //Check if user already exists
                val userIndex = users.indexOfFirst { it.name == user.name }
                if (userIndex != -1) {
                    users[userIndex] = user
                } else {
                    users.add(user)
                }
                javaSpace.write(USER_LIST_IDENTIFIER, gson.toJson(users))

            } else {
                javaSpace.write(USER_LIST_IDENTIFIER, gson.toJson(listOf(user)))
            }
        }

    override suspend fun listenToContacts(): Flow<List<User>?> = withContext(Dispatchers.IO) {
        var oldData = listOf<User>()
        flow {
            while (true) {
                delay(1000)
                val contactsList = getContactList()
                if (contactsList != oldData) {
                    oldData = contactsList
                    emit(contactsList)
                }
            }
        }
    }

    private fun getContactList(): List<User> {
        return javaSpace.read(USER_LIST_IDENTIFIER).toUserList()
    }

    companion object {
        private const val USER_LIST_IDENTIFIER = "user_list"
    }
}
