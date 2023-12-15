package com.example.krivets.bookhouse.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.krivets.bookhouse.AppToolBar
import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.data.MyBook
import com.example.krivets.bookhouse.ui.theme.Gray90
import com.example.krivets.bookhouse.ui.theme.Red40
import com.example.krivets.bookhouse.widgets.MySnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(drawerState: DrawerState, coroutineScope: CoroutineScope, controller: NavController)
{

    val viewModel: HomeViewModel =
        viewModel(factory = HomeViewModel.Factory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    controller.previousBackStackEntry

    HomeScreenContent(
        state = state,
        onRemove = viewModel::onClickRemove,
        onEdit = { id ->
            controller.navigate("EditPage/$id")
        },
        drawerState = drawerState,
        coroutineScope = coroutineScope,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(book: MyBook, onRemove: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(10),
        onClick = onClick,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSurface.copy(0.1F)),
       )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Column(modifier = Modifier
                .padding(12.dp)
            ) {
                book.title?.let {
                    Text(
                        modifier = Modifier
                            .width(200.dp),
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold)
                }
                book.authors?.let {
                        Text(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(bottom = 10.dp),
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp,
                            color = Gray90)

                }

                var bgColor = Red40

                Spacer(modifier = Modifier.padding(10.dp))
                IconButton(
                    onClick = {onRemove()},
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

            Box(modifier = Modifier

                .padding(10.dp)
                .size(width = 100.dp, height = 145.dp)
                .clip(RoundedCornerShape(10))
            )
            {
                AsyncImage(
                    modifier = Modifier
                        .size(165.dp),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(book.imageUrl?.replace("http:", "https:"))
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.ic_book_96),
                    placeholder = painterResource(id = R.drawable.loading_img),
                    contentDescription = stringResource(id = R.string.about_app),
                    contentScale = ContentScale.Crop
                )
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
private fun HomeScreenContent(
    state: HomeState,
    onRemove: (id: String) -> Unit,
    onEdit: (id: String?) -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {

    val snackbarVisible = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppToolBar(drawerState, coroutineScope, "My Books")
        },

    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is HomeState.DisplayingBooks -> {
                    if (state.books.isEmpty()) {
                        noOneBook("No one book in your list...")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.books, key = { it.bookId }) { item ->
                                BookItem(
                                    book = item,
                                    onRemove = {
                                        onRemove(item.bookId)
                                        snackbarVisible.value = true
                                        // Delay for a short duration to show the snackbar
                                        coroutineScope.launch {
                                            delay(2000)
                                            // Reset snackbarVisible to false after the delay
                                            snackbarVisible.value = false
                                        }},
                                    onClick = { onEdit(item.bookId) }
                                )
                            }
                        }
                    }
                }
                is HomeState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                is HomeState.Error -> Text(
                    state.e.message ?: stringResource(id = R.string.error_loading)
                )

                else -> {}
            }
        }
    }

    if (snackbarVisible.value) {
        MySnackbar(message = "Book has been removed from your books!", actionLabel = "DONE")

    }
}


@Composable
fun noOneBook(text: String)
{
    LazyColumn(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),

        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
        )
    {

        item() {
            Image(
                painter = painterResource(id = R.drawable.no_one_book),
                contentDescription = "book.name",
                contentScale = ContentScale.Crop,
            )
        }

        item() {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }

    }

}
