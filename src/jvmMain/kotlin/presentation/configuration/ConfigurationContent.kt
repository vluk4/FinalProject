package presentation.configuration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import presentation.viewmodel.contracts.MainContract

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
        modifier = Modifier.fillMaxSize().padding(horizontal = 64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navigateToContactsScreen.invoke()
                },
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(R.string.ONLINE, style = MaterialTheme.typography.h5)
            Switch(
                checked = uiState.isOnline,
                onCheckedChange = {
                    viewModel.handleEvent(ConfigurationScreenContract.Events.OnStatusChange(it))
                }
            )
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.handleEvent(MainContract.Event.SubscribeToMessagesTopic)
                viewModel.handleEvent(ConfigurationScreenContract.Events.SaveUserData)
            },
            enabled = uiState.saveButtonEnabled
        ) {
            Text(R.string.SAVE_DATA, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}