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
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MainRepositoryImpl(private val javaSpace: TuplesSpace, private val subscriber: Subscriber) : MainRepository {

    private val clientMessages: MutableMap<String, MutableList<ChatMessage>> = mutableMapOf()
    override suspend fun initializeServices() = withContext(Dispatchers.IO) {
        javaSpace.init()
        subscriber.initSubscriber()
    }

    override suspend fun subscribeToGeneralTopic(): Flow<MutableMap<String, MutableList<ChatMessage>>> =
        withContext(Dispatchers.IO) {
            callbackFlow {
                subscriber.subscribeToTopic(GENERAL_MESSAGE_TOPIC) { receivedMessage ->
                    val newMessage = receivedMessage.toChatMessage()
                    if (newMessage != null) {
                        clientMessages.getOrPut("${newMessage.sender}${newMessage.receiver}") { mutableListOf() }
                            .add(newMessage)
                        trySendBlocking(clientMessages)
                    }
                }
                awaitClose()
            }
        }

    override suspend fun sendMessageToGeneralTopic(message: ChatMessage) {
        withContext(Dispatchers.IO) {
            val messageJson = Gson().toJson(message)
            Publisher(GENERAL_MESSAGE_TOPIC, messageJson)
        }
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
        private const val GENERAL_MESSAGE_TOPIC = "general_topic"
    }
}
