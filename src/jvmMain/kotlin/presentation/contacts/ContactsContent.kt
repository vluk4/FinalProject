package presentation.contacts

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ContactsScreenContract

@Composable
fun ContactsContent(viewModel: MainViewModel, navigateToConfiguration: () -> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val userData = uiState.contactsScreenState.contacts

    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(ContactsScreenContract.Events.RequestUserData)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            IconButton(onClick = {
                navigateToConfiguration.invoke()
            }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(userData.toString(), style = MaterialTheme.typography.h6)
        }

    }
}