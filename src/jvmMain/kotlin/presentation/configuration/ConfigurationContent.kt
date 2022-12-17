package presentation.configuration

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import presentation.resources.R
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ConfigurationScreenContract

@Composable
fun ConfigurationContent(
    viewModel: MainViewModel,
    navigateToContactsScreen: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val uiState = viewModel.uiState.collectAsState().value.configurationScreenState

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            viewModel.effect.collect {
                if (it is ConfigurationScreenContract.Effect) {
                    when(it) {
                        ConfigurationScreenContract.Effect.NavigateToContactsScreen -> {
                            navigateToContactsScreen.invoke()
                        }
                        else -> Unit
                    }
                }

            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = R.string.FILL_YOUR_DATA, style = MaterialTheme.typography.h6)

        TextField(
            value = uiState.name,
            onValueChange = {
                viewModel.handleEvent(
                    ConfigurationScreenContract.Events.OnNameInputChanged(it)
                )
            },
            label = {
                Text(R.string.NAME_INPUT_LABEL)
            },
            maxLines = 1
        )

        TextField(
            value = uiState.nickname,
            onValueChange = {
                viewModel.handleEvent(
                    ConfigurationScreenContract.Events.OnNicknameInputChanged(it)
                )
            },
            label = {
                Text(R.string.NICKNAME_INPUT_LABEL)
            },
            maxLines = 1
        )

        TextField(
            value = uiState.latitude,
            onValueChange = {
                viewModel.handleEvent(
                    ConfigurationScreenContract.Events.OnLatitudeInputChanged(it)
                )
            },
            label = {
                Text(R.string.LATITUDE_INPUT_LABEL)
            },
            maxLines = 1
        )

        TextField(
            value = uiState.longitude,
            onValueChange = {
                viewModel.handleEvent(
                    ConfigurationScreenContract.Events.OnLongitudeInputChanged(it)
                )
            },
            label = {
                Text(R.string.LONGITUDE_INPUT_LABEL)
            },
            maxLines = 1
        )

        Button(onClick = {
            viewModel.handleEvent(ConfigurationScreenContract.Events.SaveUserData)
        }) {
            Text(R.string.SAVE_DATA, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}