package data.repository

import com.google.gson.Gson
import data.services.tuples.TuplesSpace
import domain.model.UserConfiguration
import domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepositoryImpl(private val javaSpace: TuplesSpace) : MainRepository {

    override suspend fun saveUserDataOnJavaSpace(userConfiguration: UserConfiguration) =
        withContext(Dispatchers.IO) {
            javaSpace.init()
            val stringData = Gson().toJson(userConfiguration)
            javaSpace.write(JAVA_SPACE_IDENTIFIER, stringData)
        }


    companion object {
        private const val JAVA_SPACE_IDENTIFIER = "user_configuration"
    }
}