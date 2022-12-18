package presentation.viewmodel.contracts

import presentation.viewmodel.base.UiEffect
import presentation.viewmodel.base.UiEvent
import presentation.viewmodel.base.UiState

object MainContract {

    sealed interface Event : UiEvent

    sealed interface Effect : UiEffect

    data class State(
        val contactsScreenState: ContactsScreenContract.State = ContactsScreenContract.State(),
        val configurationScreenState: ConfigurationScreenContract.State = ConfigurationScreenContract.State(),
        val chatScreenState: ChatScreenContract.State = ChatScreenContract.State()
    ) : UiState
}