package presentation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Screen : Parcelable {

    @Parcelize
    object Configuration : Screen()

    @Parcelize
    object Chat : Screen()

    @Parcelize
    object ContactsList : Screen()
}