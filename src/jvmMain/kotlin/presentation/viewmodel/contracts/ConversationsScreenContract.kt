package presentation.viewmodel.contracts

import presentation.viewmodel.base.UiState

object ConversationsScreenContract {

    sealed class Events : MainContract.Event {

    }

    data class State(
        val dummy: String = ""
    ) : UiState

    sealed class Effect : MainContract.Effect {

    }
}