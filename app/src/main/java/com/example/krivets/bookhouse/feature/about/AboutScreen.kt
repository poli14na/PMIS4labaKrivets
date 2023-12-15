package com.example.krivets.bookhouse.feature.about

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.krivets.bookhouse.AppToolBar
import com.example.krivets.bookhouse.MainActivity
import com.example.krivets.bookhouse.R
import com.example.krivets.bookhouse.darkMode
import com.example.krivets.bookhouse.ui.theme.Gray90
import com.example.krivets.bookhouse.ui.theme.NightSelectedTextColor
import com.example.krivets.bookhouse.widgets.navigationItems
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutScreen(drawerState: DrawerState, coroutineScope: CoroutineScope) {

    Scaffold(
        topBar = {
            AppToolBar(drawerState, coroutineScope, navigationItems[1].title)
        }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth()
        )
        {

            OrientationScreenAbout()
        }
    }
}


@Composable
fun OrientationScreenAbout()
{
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Box(modifier = Modifier.weight(2f))
                {
                    MainInfo(150, true)
                }

                Box(modifier = Modifier
                    .weight(1.5f)
                    .padding(top = 72.dp))
                {
                    ListOfLinks()
                }
            }
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.Center
            )
            {
                MainInfo(250, false)
                ListOfLinks()
                Text(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.version),
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray90
                )
            }
        }
    }
}

@Composable
fun ListOfLinks() {
    LazyColumn()
    {

        var image = MainActivity.dayThemeImage
        if(darkMode) image = MainActivity.nightThemeImage
        val itemCount = image.size

        items(itemCount) { item ->
            ColumnItem(
                itemIndex = item,
                painter = image,
                title = MainActivity.names,
                description = MainActivity.descriptions,
            )
        }
    }
}

@Composable
fun ColumnItem(

    itemIndex: Int,
    painter: List<Int>,
    title: List<Int>,
    description: List<Int>,
){
    Card( modifier = Modifier
        .padding(10.dp)
        .wrapContentSize(),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(3.dp, MaterialTheme.colorScheme.background)
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(color = MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.spacedBy(15.dp))
        {
            Image(
                painter = painterResource(id = painter[itemIndex]),

                contentDescription = stringResource(id = title[itemIndex]),
                modifier = Modifier
                    .size(43.dp)
                    .padding(5.dp)

            )
            Column {
                Modifier
                    .padding(10.dp)
                Text(
                    text = stringResource(id = title[itemIndex]),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = stringResource(id = description[itemIndex]),
                    color = Gray90,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun MainInfo(imageSize: Int, viewVersion: Boolean) {

    var image = R.drawable.about_3
    if(darkMode) image = R.drawable.about
    Column()
    {
        Image(
            painter = painterResource(id = image),
            contentDescription = "about",
            Modifier
                .fillMaxWidth()
                .padding(top = 60.dp)
                .size(imageSize.dp)
        )
        Text(
            buildAnnotatedString {
                append(stringResource(id = R.string.app_name))
                addStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold),
                    start = 0,
                    end = 1
                )
                addStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold),
                    start = 4,
                    end = 5
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.author),
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.about_app),
            color = Gray90,
            style = MaterialTheme.typography.bodyLarge,
        )
        if(viewVersion)
        {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.version),
                style = MaterialTheme.typography.bodySmall,
                color = NightSelectedTextColor

            )
        }
    }
}