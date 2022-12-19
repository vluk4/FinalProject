package presentation.configuration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
                    when (it) {
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = R.string.FILL_YOUR_DATA, style = MaterialTheme.typography.h6)

        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(R.string.ONLINE, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.width(32.dp))
            Switch(
                checked = uiState.isOnline,
                onCheckedChange = {
                    viewModel.handleEvent(ConfigurationScreenContract.Events.OnStatusChange(it))
                }
            )
        }

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
            value = uiState.radius,
            onValueChange = {
                viewModel.handleEvent(
                    ConfigurationScreenContract.Events.OnSearchRadiusInputChanged(it)
                )
            },
            label = {
                Text(R.string.RADIUS_INPUT_LABEL)
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
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
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Button(onClick = {
            viewModel.handleEvent(ConfigurationScreenContract.Events.SaveUserData)
        }) {
            Text(R.string.SAVE_DATA, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}