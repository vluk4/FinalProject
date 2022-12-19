package presentation.viewmodel

import domain.interactor.InitializeFeatureResults
import domain.interactor.MainInteractor
import domain.interactor.SaveUserDataResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import presentation.viewmodel.base.BaseViewModel
import presentation.viewmodel.contracts.*

class MainViewModel(
    private val coroutineScope: CoroutineScope,
    private val interactor: MainInteractor
) :
    BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>(coroutineScope) {
    override fun createInitialState() = MainContract.State()

    override fun handleEvent(event: MainContract.Event) {
        coroutineScope.launch {
            when (event) {
                is LoadingScreenContract.Events -> {
                    processLoadingScreenEvents(event)
                }

                is ContactsScreenContract.Events -> {
                    processConversationScreenEvents(event)
                }

                is ChatScreenContract.Events -> {
                    processChatScreenEvents(event)
                }

                is ConfigurationScreenContract.Events -> {
                    processConfigurationScreenEvents(event)
                }

                else -> Unit
            }
        }
    }

    private suspend fun processLoadingScreenEvents(event: LoadingScreenContract.Events) {
        when(event) {
            is LoadingScreenContract.Events.InitializeApplication -> {
                val effect = when(interactor.initializeApplication()) {
                    is InitializeFeatureResults.SuccessfullyInitialized -> {
                            LoadingScreenContract.Effect.ProceedToConfigurationScreen
                    }
                    is InitializeFeatureResults.FailedToInitialize -> {
                            LoadingScreenContract.Effect.HandleError
                    }
                }
                setEffect { effect }
            }
        }
    }

    private fun processChatScreenEvents(event: ChatScreenContract.Events) {

    }

    private suspend fun processConversationScreenEvents(event: ContactsScreenContract.Events) {
        when (event) {
            ContactsScreenContract.Events.RequestUserData -> {
                interactor.getUserData().collect {
                    setState {
                        copy(
                            contactsScreenState = currentState.contactsScreenState.copy(
                                contacts = it
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun processConfigurationScreenEvents(event: ConfigurationScreenContract.Events) {
        when (event) {
            is ConfigurationScreenContract.Events.OnNameInputChanged -> {
                setState {
                    copy(
                        configurationScreenState = currentState.configurationScreenState.copy(
                            name = event.name
                        )
                    )
                }
            }

            is ConfigurationScreenContract.Events.OnNicknameInputChanged -> {
                setState {
                    copy(
                        configurationScreenState = currentState.configurationScreenState.copy(
                            nickname = event.nickname
                        )
                    )
                }
            }

            is ConfigurationScreenContract.Events.OnLatitudeInputChanged -> {
                setState {
                    copy(
                        configurationScreenState = currentState.configurationScreenState.copy(
                            latitude = event.latitude
                        )
                    )
                }
            }

            is ConfigurationScreenContract.Events.OnLongitudeInputChanged -> {
                setState {
                    copy(
                        configurationScreenState = currentState.configurationScreenState.copy(
                            longitude = event.longitude
                        )
                    )
                }
            }

            ConfigurationScreenContract.Events.SaveUserData -> {
                with(currentState.configurationScreenState) {
                    val result = interactor.saveUserDate(
                        isOnline = isOnline,
                        name = name,
                        radius = radius,
                        nickname = nickname,
                        latitude = latitude,
                        longitude = longitude
                    )

                    val effect = when (result) {
                        is SaveUserDataResults.SuccessfullySavedData -> {
                            ConfigurationScreenContract.Effect.NavigateToContactsScreen
                        }

                        is SaveUserDataResults.FailedToSavedData -> {
                            ConfigurationScreenContract.Effect.ShowUnexpectedError
                        }
                    }
                    setEffect { effect }
                }
            }

            is ConfigurationScreenContract.Events.OnSearchRadiusInputChanged -> {
                setState {
                    copy(
                        configurationScreenState = currentState.configurationScreenState.copy(
                            radius = event.radius
                        )
                    )
                }
            }

            is ConfigurationScreenContract.Events.OnStatusChange -> {
                setState {
                    copy(
                        configurationScreenState = configurationScreenState.copy(
                            isOnline = event.isOnline
                        )
                    )
                }
            }
        }
    }
}