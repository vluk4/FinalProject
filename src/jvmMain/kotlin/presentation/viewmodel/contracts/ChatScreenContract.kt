package presentation.viewmodel.contracts

import domain.model.ChatMessage
import domain.model.User
import presentation.viewmodel.base.UiState

object ChatScreenContract {

    sealed class Events : MainContract.Event {
        object OnMessageSend: Events()
        object SubscribeToChat : Events()
        object CleanState : Events()
        data class OnMessageInputChanged(val message: String): Events()
    }

    data class State(
        val messages: List<ChatMessage> = listOf(),
        val message: String = "",
        val receiver: User = User()
    ) : UiState

    sealed class Effect : MainContract.Effect {

    }
}