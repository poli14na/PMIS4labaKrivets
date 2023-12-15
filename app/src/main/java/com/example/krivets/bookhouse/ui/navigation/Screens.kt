package com.example.krivets.bookhouse.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import com.example.krivets.bookhouse.feature.about.AboutScreen
import com.example.krivets.bookhouse.feature.books.BooksHome
import com.example.krivets.bookhouse.feature.edit.EditScreen
import com.example.krivets.bookhouse.feature.home.HomeScreen
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(drawerState: DrawerState, coroutineScope: CoroutineScope, navController: NavController) {

    HomeScreen(drawerState, coroutineScope, navController)
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutPage(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    AboutScreen(drawerState, coroutineScope)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BooksPage(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    BooksHome(drawerState, coroutineScope)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditPage(id: String?, navController: NavController, coroutineScope: CoroutineScope,) {
    EditScreen(id, navController = navController, coroutineScope)
}




