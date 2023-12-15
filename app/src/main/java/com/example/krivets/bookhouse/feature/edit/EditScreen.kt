package com.example.krivets.bookhouse.feature.edit

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.krivets.bookhouse.BookHouse
import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.data.BookNote
import com.example.krivets.bookhouse.feature.home.noOneBook
import com.example.krivets.bookhouse.ui.theme.Gray90
import com.example.krivets.bookhouse.widgets.MySnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    id: String?,
    navController: NavController,
    coroutineScope: CoroutineScope,
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(true) }

    val context = LocalContext.current
    val application = context.applicationContext as BookHouse

    val viewModel: NotesBookViewModel = remember {
        NotesBookViewModel.create(application, id ?: "")
    }

    val state by viewModel.state.collectAsStateWithLifecycle()


            EditBookContent(
                state = state,
                onAdd = viewModel::addANote,
                onDelete = viewModel::deleteNote,
                coroutineScope= coroutineScope
            )



}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun EditBookContent(
    state: BookState,
    onAdd: (String, String) -> Unit,
    onDelete: (id: String) -> Unit,
    coroutineScope: CoroutineScope,
) {

    val showDialog = remember { mutableStateOf(false) }
    val snackbarVisible = remember { mutableStateOf(false) }
   Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
           FloatingActionButton(

               onClick = { showDialog.value = true },
               modifier = Modifier
                   .size(70.dp)
                   .padding(5.dp)


           ) {
               Icon(
                   imageVector = Icons.Rounded.Add,
                   contentDescription = "add book",
                   tint = Color.White
               )
           } },
       topBar =  { CenterAlignedTopAppBar(
           title = {
               Text(
                   text = "Notes",
                   color = MaterialTheme.colorScheme.primary,
                   style = MaterialTheme.typography.titleLarge,
               )
           },

       )
   }

    ) {

        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 800.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is BookState.Error -> Text(
                    state.e.message ?: stringResource(id = R.string.app_name)
                )
                is BookState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                is BookState.DisplayingNotes -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.matchParentSize()
                ) {



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                        ) {
                            state.book?.title?.let {
                                Text(
                                    modifier = Modifier
                                        .width(200.dp),
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            state.book?.authors?.let {
                                Text(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .padding(bottom = 10.dp),
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 15.sp,
                                    color = Gray90
                                )

                            }

                            if(showDialog.value)
                            {
                                state.book?.let { DialogWindow(showDialog = showDialog, onAdd, it.bookId) }
                            }


                        }

                        Box(
                            modifier = Modifier

                                .padding(10.dp)
                                .size(width = 100.dp, height = 145.dp)
                                .clip(RoundedCornerShape(10))
                        )
                        {
                            AsyncImage(
                                modifier = Modifier
                                    .size(165.dp),
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(state.book?.imageUrl?.replace("http:", "https:"))
                                    .crossfade(true)
                                    .build(),
                                error = painterResource(id = R.drawable.ic_book_96),
                                placeholder = painterResource(id = R.drawable.loading_img),
                                contentDescription = stringResource(id = R.string.about_app),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (snackbarVisible.value) {
                        MySnackbar(message = "Note has been removed!", actionLabel = "DONE")

                    }

                    if(state.textNote.isEmpty())
                    {
                        noOneBook(text = "No one notes in your list...")

                    }


                    else {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            items(state.textNote, key = { it.uuid }) { item ->
                                NoteItem(
                                    note = item,
                                    onDelete = {
                                        onDelete(item.uuid)
                                        snackbarVisible.value = true
                                        // Delay for a short duration to show the snackbar
                                        coroutineScope.launch {
                                            delay(2000)
                                            // Reset snackbarVisible to false after the delay
                                            snackbarVisible.value = false
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                    }




                }


            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: BookNote,
    onDelete: () -> Unit)
{
    Row( verticalAlignment = Alignment.CenterVertically)
    {
        TextField(
            modifier = Modifier.padding(horizontal = 10.dp),
            value = note.text,
            onValueChange = {},

            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.labelMedium.fontSize
            ),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White),
           )

        IconButton(
            onClick = {onDelete()},
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(30))
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)

        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "delete",
                tint = Color.White
            )
        }

    }


}

@Composable
fun DialogWindow(showDialog: MutableState<Boolean>,
                 onAdd: (String, String) -> Unit,
                 bookId: String)
{
    val noteText = remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("Add Note") },

        text = {
            TextField(
                value = noteText.value,
                onValueChange = { noteText.value = it },
                label = { Text("Enter note") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdd(noteText.value, bookId)
                    showDialog.value = false
                }
            ) {
                Text(text = "Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = { showDialog.value = false }
            ) {
                Text(text = "Cancel", color = Color.White)
            }
        }
    )
}