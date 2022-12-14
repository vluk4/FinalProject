package presentation.chat

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import domain.model.ChatMessage
import kotlinx.coroutines.launch
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ChatScreenContract

@Composable
fun ChatContent(viewModel: MainViewModel, onBackPressed: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    val messages = uiState.messages
    val input = uiState.chatScreenState.message
    val currentUser = uiState.currentUser
    val receiver = uiState.chatScreenState.receiver
    val scrollState = rememberScrollState()

    val type = object : TypeToken<MutableMap<String, MutableList<ChatMessage>>>() {}.type
    val messagesObject = Gson().fromJson<MutableMap<String, MutableList<ChatMessage>>>(messages, type) ?: emptyMap()

    val myMessages = messagesObject.getOrDefault("${currentUser.name}${receiver.name}", mutableListOf())
    val receiverMessages = messagesObject.getOrDefault("${receiver.name}${currentUser.name}", mutableListOf())

    val chatMessages = (myMessages + receiverMessages).sortedBy { it.date }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            ChatToolbar(receiver.name, onBackPressed = onBackPressed)

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .verticalScroll(scrollState).padding(vertical = 48.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chatMessages.forEach { message ->
                    if (message.sender == currentUser.name) {
                        MessageCard(
                            modifier = Modifier.align(Alignment.End).padding(start = 48.dp),
                            color = Color.DarkGray,
                            message = message
                        )
                    } else {
                        MessageCard(
                            modifier = Modifier.align(Alignment.Start).padding(end = 48.dp),
                            color = Color.Blue,
                            message = message
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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

@Composable
private fun ChatToolbar(title: String, onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = Color.LightGray),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBackPressed.invoke() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = title,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
private fun MessageCard(modifier: Modifier = Modifier, color: Color, message: ChatMessage) {
    Card(modifier = modifier, backgroundColor = color, shape = RoundedCornerShape(CornerSize(percent = 25))) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = message.message,
                color = Color.White,
                style = MaterialTheme.typography.body1
            )

            Text(
                modifier = modifier.padding(8.dp),
                text = message.hours,
                color = Color.LightGray,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview
@Composable
fun ChatMessagePreview() {
    MaterialTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(color = MaterialTheme.colors.background)
        ) {
            MessageCard(
                color = Color.Green,
                message = ChatMessage(
                    sender = "Victor",
                    message = "Ola tudo bem?"
                )
            )
            MessageCard(
                color = Color.Blue,
                message = ChatMessage(
                    sender = "Victor",
                    message = "Tudo sim e com voce?"
                )
            )
        }
    }
}