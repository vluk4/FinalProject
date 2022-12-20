package domain.interactor

import domain.model.User

sealed interface SaveUserDataResults {
    data class SuccessfullySavedData(val user: User) : SaveUserDataResults
    object FailedToSavedData : SaveUserDataResults
}

sealed interface InitializeFeatureResults {
    object SuccessfullyInitialized : InitializeFeatureResults
    object FailedToInitialize : InitializeFeatureResults
}