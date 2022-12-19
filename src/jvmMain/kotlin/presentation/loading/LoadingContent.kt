package presentation.loading

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import presentation.resources.R
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.LoadingScreenContract

@Composable
fun LoadingContent(viewModel: MainViewModel, onLoadingFinished: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {

        viewModel.handleEvent(LoadingScreenContract.Events.InitializeApplication)

        coroutineScope.launch {
            viewModel.effect.collect {
                if (it is LoadingScreenContract.Effect) {
                    when (it) {
                        LoadingScreenContract.Effect.ProceedToConfigurationScreen -> {
                            onLoadingFinished.invoke()
                        }

                        else -> Unit
                    }
                }

            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = R.string.LOADING_CONFIGURATION, style = MaterialTheme.typography.h5)
        }
    }
}