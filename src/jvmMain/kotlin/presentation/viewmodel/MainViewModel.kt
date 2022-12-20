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
        when (event) {
            is LoadingScreenContract.Events.InitializeApplication -> {
                val effect = when (interactor.initializeApplication()) {
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

    private suspend fun processChatScreenEvents(event: ChatScreenContract.Events) {
        when (event) {
            is ChatScreenContract.Events.OnMessageInputChanged -> {
                setState {
                    copy(
                        chatScreenState = currentState.chatScreenState.copy(
                            message = event.message
                        )
                    )
                }
            }

            ChatScreenContract.Events.OnMessageSend -> {
                val result = interactor.sendAndGetMessages(
                    currentState.chatScreenState.message,
                    currentState.currentUser,
                    currentState.chatScreenState.receiver
                )
                setState {
                    copy(
                        chatScreenState = currentState.chatScreenState.copy(
                            messages = result?.map { it }?.toMutableList().orEmpty(),
                            message = ""
                        )
                    )
                }
            }

            ChatScreenContract.Events.SubscribeToChat -> {
                interactor.subscribeToChat(
                    currentState.currentUser.name,
                    currentState.chatScreenState.receiver.name
                ).collect {
                    setState {
                        copy(
                            chatScreenState = currentState.chatScreenState.copy(
                                messages = it?.map { it }?.toMutableList().orEmpty()
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun processConversationScreenEvents(event: ContactsScreenContract.Events) {
        when (event) {
            ContactsScreenContract.Events.RequestUserData -> {
                interactor.getUserData().collect { users ->
                    val filteredList = users?.filter { it.name != currentState.currentUser.name }
                    setState {
                        copy(
                            contactsScreenState = currentState.contactsScreenState.copy(
                                contacts = filteredList
                            )
                        )
                    }
                }
            }

            is ContactsScreenContract.Events.OnChatSelected -> {
                setState {
                    copy(
                        chatScreenState = currentState.chatScreenState.copy(
                            receiver = event.user
                        )
                    )
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
                            setState {
                                copy(currentUser = result.user)
                            }
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
                        configurationScreenState = currentState.configurationScreenState.copy(
                            isOnline = event.isOnline
                        )
                    )
                }
            }
        }

        with(currentState.configurationScreenState) {
            val enableSaveButton = interactor.shouldEnableSaveButton(name, nickname, radius, latitude, longitude)
            setState {
                copy(
                    configurationScreenState = configurationScreenState.copy(
                        saveButtonEnabled = enableSaveButton
                    )
                )
            }
        }
    }
}