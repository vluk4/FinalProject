package presentation.viewmodel.contracts

import domain.model.User
import presentation.viewmodel.base.UiState
import presentation.viewmodel.contracts.MainContract.Event

object ContactsScreenContract {

    sealed class Events : Event {
        data class OnChatSelected(val user: User) : Events()
        object RequestUserData: Events()
    }

    data class State(
        val contacts: List<User>? = listOf()
    ) : UiState

    sealed class Effect : MainContract.Effect {

    }
}