package presentation.viewmodel.contracts

import jdk.jfr.Enabled
import presentation.viewmodel.base.UiState

object ConfigurationScreenContract {
    sealed class Events : MainContract.Event {
        object SaveUserData : Events()
        data class OnStatusChange(val isOnline: Boolean) : Events()
        data class OnNameInputChanged(val name: String) : Events()
        data class OnNicknameInputChanged(val nickname: String) : Events()
        data class OnLatitudeInputChanged(val latitude: String) : Events()
        data class OnLongitudeInputChanged(val longitude: String) : Events()
        data class OnSearchRadiusInputChanged(val radius: String) : Events()

    }

    data class State(
        val isOnline: Boolean = false,
        val name: String = "",
        val nickname: String = "",
        val radius: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val saveButtonEnabled: Boolean = false
    ) : UiState

    sealed class Effect : MainContract.Effect {
        object NavigateToContactsScreen: Effect()
        object ShowUnexpectedError: Effect()
    }
}