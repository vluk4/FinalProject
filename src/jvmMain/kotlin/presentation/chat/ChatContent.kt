package presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ChatScreenContract

@Composable
fun ChatContent(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val messages = uiState.chatScreenState.messages.toMutableStateList()
    val input = uiState.chatScreenState.message
    val currentUser = uiState.currentUser


    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(ChatScreenContract.Events.SubscribeToChat)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            messages.forEach { message ->
                if (message.sender == currentUser.name) {
                    Text(message.message, color = Color.Green)

                } else {
                    Text(message.message, color = Color.Red)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = input,
                onValueChange = {
                    viewModel.handleEvent(ChatScreenContract.Events.OnMessageInputChanged(it))
                },
                modifier = Modifier.weight(10f),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.handleEvent(ChatScreenContract.Events.OnMessageSend)
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
            )
            IconButton(
                onClick = {
                    viewModel.handleEvent(ChatScreenContract.Events.OnMessageSend)
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        }
    }

}