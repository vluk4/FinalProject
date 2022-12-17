package domain.interactor

sealed interface SaveUserDataResults {
    object SuccessfullySavedData: SaveUserDataResults
    object FailedToSavedData: SaveUserDataResults
}