package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import presentation.configuration.ConfigurationContent
import presentation.contacts.ContactsContent
import presentation.loading.LoadingContent
import presentation.navigation.ChildStack
import presentation.viewmodel.MainViewModel

@Composable
fun MainContent(viewModel: MainViewModel) {
    val navigation = remember { StackNavigation<Screen>() }

    ChildStack(
        source = navigation,
        initialStack = { listOf(Screen.Loading) },
        animation = stackAnimation(fade() + scale()),
    ) { screen ->
        when (screen) {
            is Screen.Configuration -> {
                ConfigurationContent(
                    viewModel = viewModel,
                    navigateToContactsScreen = { navigation.replaceCurrent(Screen.ContactsList) })
            }

            is Screen.ContactsList -> {
                ContactsContent(
                    viewModel = viewModel,
                    navigateToConfiguration = { navigation.replaceCurrent(Screen.Configuration) })
            }

            Screen.Loading -> {
                LoadingContent(
                    viewModel = viewModel,
                    onLoadingFinished = { navigation.replaceCurrent(Screen.Configuration) }
                )
            }

            Screen.Chat -> Unit
        }
    }
}