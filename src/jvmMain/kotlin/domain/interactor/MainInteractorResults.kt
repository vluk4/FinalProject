package domain.interactor

sealed interface SaveUserDataResults {
    object SuccessfullySavedData: SaveUserDataResults
    object FailedToSavedData: SaveUserDataResults
}

sealed interface InitializeFeatureResults {
    object SuccessfullyInitialized: InitializeFeatureResults
    object FailedToInitialize: InitializeFeatureResults
}