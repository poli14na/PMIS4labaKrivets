package com.example.krivets.bookhouse.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetM3() {

    var canShowSnackBar by remember {
        mutableStateOf(false)
    }

    var openBottomSheet by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (openBottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = { openBottomSheet = false; canShowSnackBar = true },

        ) {
            BottomSheetContent(
                onHideButtonClick = {
                    canShowSnackBar = true
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) openBottomSheet = false
                    }
                },
            )
        }
    }

    if(canShowSnackBar) {

        var messageText = "Information about this book has changed."
        MySnackbar(
            message = messageText,
            actionLabel = "Undone"
        )

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    onHideButtonClick: () -> Unit,
)
{
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),

        modifier = Modifier
            .fillMaxWidth()

    )
    {

        var textBottomSheet = "Changes have been saved successfully!"

        item()
        {
            Text(
                text = textBottomSheet,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp)

        }
        item()
        {
            Button(onClick = onHideButtonClick,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .width(100.dp))

            {
                Text(text = "ОК",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White)

            }
        }
    }

}


@Composable
fun MySnackbar(
    message: String,
    actionLabel: String,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(message, actionLabel, duration = duration)

    }
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(
            hostState = snackbarHostState,

        )
    }


}