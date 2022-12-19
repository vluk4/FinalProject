package presentation.viewmodel.contracts

import presentation.viewmodel.base.UiState

object LoadingScreenContract {

    sealed class Events : MainContract.Event {
        object InitializeApplication: Events()
    }

    data class State(
        val dummy: String = ""
    ) : UiState

    sealed class Effect : MainContract.Effect {
        object ProceedToConfigurationScreen: Effect()
        object HandleError: Effect()
    }
}