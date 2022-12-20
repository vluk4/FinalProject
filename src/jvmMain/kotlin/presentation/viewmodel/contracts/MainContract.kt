package presentation.viewmodel.contracts

import domain.model.User
import presentation.viewmodel.base.UiEffect
import presentation.viewmodel.base.UiEvent
import presentation.viewmodel.base.UiState

object MainContract {

    sealed interface Event : UiEvent

    sealed interface Effect : UiEffect

    data class State(
        val currentUser: User = User(),
        val contactsScreenState: ContactsScreenContract.State = ContactsScreenContract.State(),
        val configurationScreenState: ConfigurationScreenContract.State = ConfigurationScreenContract.State(),
        val chatScreenState: ChatScreenContract.State = ChatScreenContract.State()
    ) : UiState
}