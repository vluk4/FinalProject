package data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import data.services.tuples.TuplesSpace
import domain.model.User
import domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MainRepositoryImpl(private val javaSpace: TuplesSpace) : MainRepository {

    override suspend fun updateUserData(user: User) =
        withContext(Dispatchers.IO) {
            javaSpace.init()
            val gson = Gson()
            val type = object : TypeToken<List<User>>() {}.type
            val userListTuple = javaSpace.take(USER_LIST_IDENTIFIER)
            if (!userListTuple.isNullOrEmpty()) {
                val users = gson.fromJson<List<User>>(userListTuple, type).toMutableList()
                val userIndex = users.indexOfFirst { it.name == user.name}
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
        val gson = Gson()
        val type = object : TypeToken<List<User>>() {}.type

        return runCatching {
            val tuple = javaSpace.read(USER_LIST_IDENTIFIER)
            gson.fromJson<List<User>>(tuple, type)
        }.getOrElse {
            listOf()
        }
    }


    companion object {
        private const val USER_LIST_IDENTIFIER = "user_list"
    }
}