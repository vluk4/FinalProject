package presentation.viewmodel.contracts

import presentation.viewmodel.base.UiState

object ConfigurationScreenContract {
    sealed class Events : MainContract.Event {
        object SaveUserData : Events()
        data class OnNameInputChanged(val name: String) : Events()
        data class OnNicknameInputChanged(val nickname: String) : Events()
        data class OnLatitudeInputChanged(val latitude: String) : Events()
        data class OnLongitudeInputChanged(val longitude: String) : Events()

    }

    data class State(
        val name: String = "",
        val nickname: String = "",
        val latitude: String = "",
        val longitude: String = ""
    ) : UiState

    sealed class Effect : MainContract.Effect {
        object NavigateToContactsScreen: Effect()
        object ShowUnexpectedError: Effect()
    }
}