package com.moneybudget

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.moneybudget.analyses.AnalysesContent
import com.moneybudget.repository.Repository
import com.moneybudget.ui.theme.MoneyBudgetTheme
import com.moneybudget.utils.*
import kotlinx.coroutines.launch


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.initialize(this.applicationContext)

        setContent {
            val navController = rememberNavController()

            val activity = this
            MoneyBudgetTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.LockScreen.route
                ) {
                    composable(Screen.Overview.route) {
                        MainComposable()
                    }
                    composable(Screen.LockScreen.route) {
                        if(PreferenceManager
                                .getDefaultSharedPreferences(activity.applicationContext)
                                .getBoolean("lockApplication", false)) {
                            LockApplication(activity) {
                                navController.navigate(Screen.Overview.route) {
                                        popUpTo(Screen.LockScreen.route) { inclusive = true }
                                }
                            }
                        }
                        else {
                            MainComposable()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainComposable() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalDrawer(
        gesturesEnabled = true,
        drawerState = scaffoldState.drawerState,
        drawerContent = {
            DrawerContent(navController, scaffoldState, coroutineScope)
        },
        content = {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }) {
                                Icon(Icons.Rounded.Menu, contentDescription = null)
                            }
                        })
                },
            ) {
                NavHost(navController, startDestination = Screen.Overview.route) {
                    operationsGraph(navController)

                    categoriesGraph(navController)

                    accountsGraph(navController)

                    categoryTypesGraph(navController)

                    composable(Screen.Analyses.route) { AnalysesContent(true, navController) }

                    composable(Screen.Overview.route) { AnalysesContent(false, navController)  }
                }
            }
        }
    )
}