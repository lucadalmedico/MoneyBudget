package com.moneybudget.utils

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.moneybudget.R
import com.moneybudget.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController : NavHostController,
                  scaffoldState : ScaffoldState,
                  coroutineScope : CoroutineScope) {
    val drawerElements = listOf(
        Pair(Screen.Overview, Icons.Default.Home),
        Pair(Screen.OperationsRoute, Icons.Default.Paid),
        Pair(Screen.AccountsRoute,Icons.Default.AccountBalanceWallet),
        Pair(Screen.CategoriesRoute, Icons.Default.FilterNone),
        Pair(Screen.CategoryTypesRoute, Icons.Default.CollectionsBookmark),
        Pair(Screen.Analyses, Icons.Default.Analytics)
    )
    val context = LocalContext.current
    val intent = Intent(LocalContext.current, Settings::class.java)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.padding(4.dp))

        drawerElements.forEach { screenInfo ->
            DrawerElement(
                imageVector = screenInfo.second,
                text = stringResource(screenInfo.first.resourceId),
                onClick = {
                    navController.navigate(screenInfo.first.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                },
                currentDestination?.hierarchy?.any { it.route == screenInfo.first.route } == true,
            )
        }

        DrawerElement(
            imageVector = Icons.Default.Settings,
            text = stringResource(R.string.preferences),
            onClick = { context.startActivity(intent) },
            false
        )
    }
}

@Composable
fun DrawerElement(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean
) {

    var modifier = Modifier
        .padding(PaddingValues(16.dp, 4.dp, 8.dp, 4.dp))
        .clickable(
            onClick = { onClick() })
        .fillMaxWidth()

    if (isSelected) {
        modifier = modifier
            .background(
                MaterialTheme.colors.secondaryVariant,
                RoundedCornerShape(2.dp))
    }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = imageVector,
            contentDescription = text
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.body1
        )
    }
}
