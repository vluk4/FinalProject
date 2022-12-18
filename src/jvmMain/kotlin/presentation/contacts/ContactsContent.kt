package presentation.contacts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import presentation.viewmodel.MainViewModel
import presentation.viewmodel.contracts.ContactsScreenContract

@Composable
fun ContactsContent(viewModel: MainViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val userData = uiState.contactsScreenState.contacts

    LaunchedEffect(key1 = Unit) {
        viewModel.handleEvent(ContactsScreenContract.Events.RequestUserData)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(userData.toString(), style = MaterialTheme.typography.h3)
    }
}