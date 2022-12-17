import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import data.repository.MainRepositoryImpl
import data.services.tuples.TuplesSpace
import domain.interactor.MainInteractor
import domain.interactor.MainInteractorImpl
import domain.repository.MainRepository
import presentation.MainContent
import presentation.navigation.ProvideComponentContext
import presentation.viewmodel.MainViewModel

fun main() {

    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)

    val javaSpace = TuplesSpace()
    val repository: MainRepository = MainRepositoryImpl(javaSpace)
    val interactor: MainInteractor =  MainInteractorImpl(repository)

    application {

        val coroutineScope = rememberCoroutineScope()

        val viewModel = remember { MainViewModel(coroutineScope, interactor) }

        val windowState = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition(Alignment.Center),
        )
        Window(
            state = windowState,
            onCloseRequest = ::exitApplication,
            title = "Projeto Final"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                        ProvideComponentContext(rootComponentContext) {
                            MainContent(viewModel)
                        }
                    }
                }
            }
        }
    }
}
