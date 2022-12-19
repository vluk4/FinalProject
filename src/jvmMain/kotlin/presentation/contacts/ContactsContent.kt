package presentation.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.User
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ContactsScreenContract

@Composable
fun ContactsContent(viewModel: MainViewModel, navigateToConfiguration: () -> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val userData = uiState.contactsScreenState.contacts

    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(ContactsScreenContract.Events.RequestUserData)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            IconButton(
                modifier = Modifier.padding(start = 16.dp),
                onClick = { navigateToConfiguration.invoke() })
            {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ContactsList(modifier = Modifier.fillMaxWidth(), contacts = userData)
        }

    }
}

@Composable
private fun ContactsList(modifier: Modifier = Modifier, contacts: List<User>?) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        contacts?.forEach { contact ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    elevation = 10.dp
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                text = contact.name,
                                style = MaterialTheme.typography.h6
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp),
                                text = contact.nickname,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }

                        if (contact.isOnline) {
                            Text(
                                text = "Online",
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                color = Color.Green
                            )
                        } else {
                            Text(text = "Offline", modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}