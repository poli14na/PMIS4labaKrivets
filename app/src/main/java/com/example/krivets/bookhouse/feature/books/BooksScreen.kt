package com.example.krivets.bookhouse.feature.books

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.krivets.bookhouse.AppToolBar
import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.data.Book
import com.example.krivets.bookhouse.ui.theme.Gray90
import com.example.krivets.bookhouse.ui.theme.Green40
import com.example.krivets.bookhouse.widgets.MainAppBar
import com.example.krivets.bookhouse.widgets.MySnackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun BooksHome(
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    val booksViewModel: BooksViewModel =
        viewModel(factory = BooksViewModel.Factory)
    val searchWidgetState = booksViewModel.searchWidgetState
    val searchTextState = booksViewModel.searchTextState
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                AppToolBar(drawerState, coroutineScope, "Books")
                MainAppBar(
                    searchWidgetState = searchWidgetState.value,
                    searchTextState = searchTextState.value,
                    onTextChange = {
                        booksViewModel.updateSearchTextState(newValue = it)
                    },
                    onCloseClicked = {
                        booksViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    },
                    onSearchClicked = {
                        booksViewModel.getBooks(it)
                    },
                    onSearchTriggered = {
                        booksViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                    }
                )
            }
        }

    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            BooksScreen(
                booksUiState = booksViewModel.booksUiState,
                onClickAdd = booksViewModel::addBookToMyBooks,
                onCheckBook = booksViewModel::checkBookExists,
                retryAction = { booksViewModel.getBooks() },
                drawerState = drawerState,
                coroutineScope = coroutineScope,
            )
        }
    }
}

@Composable
fun BooksScreen(
    booksUiState: BooksUiState,
    onClickAdd: (Book) -> Unit,
    onCheckBook: (Book) -> Boolean,
    retryAction: () -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        when (booksUiState) {
            is BooksUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            is BooksUiState.Success -> {
                BooksScreenContent(
                    books = booksUiState.bookSearch,
                    onClickAdd = onClickAdd,
                    onCheckBook = onCheckBook,
                    drawerState = drawerState,
                    coroutineScope = coroutineScope,
                )

            }
            is BooksUiState.Error -> ErrorScreen(retryAction = retryAction, modifier = Modifier)

            else -> {}
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BooksScreenContent(
    books: List<Book>,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    onClickAdd: (Book) -> Unit,
    onCheckBook: (Book) -> Boolean,// Add onClickAdd parameter
) {

    val snackbarVisible = remember { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center

        ) {
            itemsIndexed(books) { _, book ->
                BooksCard(
                    book = book,
                    onClickAdd =
                    {
                        onClickAdd(it)

                        snackbarVisible.value = true
                        // Delay for a short duration to show the snackbar
                        coroutineScope.launch {
                            delay(2000)
                            // Reset snackbarVisible to false after the delay
                            snackbarVisible.value = false
                        }
                    },
                    onCheckBook = onCheckBook

                )
            }
        }

    if (snackbarVisible.value) {
        MySnackbar(message = "Book has been added to your books!", actionLabel = "DONE")

    }

}


@Composable
fun BooksCard(
    book: Book,
    onClickAdd: (Book) -> Unit,
    onCheckBook: (Book) -> Boolean,
) {

    val addedText = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(10.dp),
        border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(10),
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

                book.authors?.let {authors ->

                    if(authors.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(bottom = 10.dp),
                            text = authors[0],
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp,
                            color = Gray90
                        )
                    }

                }

                var bgColor = Green40

                Spacer(modifier = Modifier.padding(10.dp))

                if(onCheckBook(book))
                {
                    addedText.value = true

                }

                if(addedText.value)
                {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(bgColor)

                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp, 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            text = "Added"
                        )
                    }
                }
                else{
                    IconButton(
                        onClick = {onClickAdd(book)},
                        modifier = Modifier
                            .size(35.dp)
                            .clip(RoundedCornerShape(30))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(5.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "delete",
                            tint = Color.White
                        )
                    }
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
                        .data(book.imageLink?.replace("http:", "https:"))
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


@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Image(painter = painterResource(id = R.drawable.no_one_book), contentDescription = "error network" )
        Text(text = stringResource(id = R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(text = stringResource(id = R.string.retry), color = Color.White)

        }

    }
}


