package com.moneybudget.utils

import androidx.annotation.StringRes
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.moneybudget.R
import com.moneybudget.accounts.account.Account
import com.moneybudget.accounts.accountsList.AccountsList
import com.moneybudget.categories.categoriesList.CategoriesList
import com.moneybudget.categories.category.Category
import com.moneybudget.categoryTypes.categoryType.CategoryType
import com.moneybudget.categoryTypes.categoryTypesList.CategoryTypesList
import com.moneybudget.operations.movement.Movement
import com.moneybudget.operations.operation.Operation
import com.moneybudget.operations.operationsList.OperationsAndMovementsList

sealed class Screen(val route: String,
                    @StringRes val resourceId: Int) {
    object Operation : Screen("operation", R.string.operation)
    object Operations : Screen("operations", R.string.operations)
    object Movement : Screen("movement", R.string.movement)

    object Categories : Screen("categories", R.string.categories)
    object Category : Screen("category", R.string.category)

    object Accounts : Screen("accounts", R.string.accounts)
    object Account : Screen("account", R.string.account)

    object CategoryTypes : Screen("categoryTypes", R.string.categoryTypes)
    object CategoryType : Screen("categoryType", R.string.categoryType)

    object Analyses : Screen("analyses", R.string.analyses)

    object Overview : Screen("overview", R.string.overview)

    object LockScreen : Screen("lock", R.string.lockApplication)

    object OperationsRoute  : Screen("operationsRoute", R.string.operations)
    object CategoriesRoute  : Screen("categoriesRoute", R.string.categories)
    object AccountsRoute    : Screen("accountsRoute", R.string.accounts)
    object CategoryTypesRoute : Screen("categoryTypesRoute", R.string.categoryTypes)
}

fun NavGraphBuilder.operationsGraph(
    navController: NavHostController) {

    navigation(
        startDestination = Screen.Operations.route,
        route = Screen.OperationsRoute.route) {

        composable(Screen.Operations.route) {
            OperationsAndMovementsList(navController)
        }

        composable(route = Screen.Operation.route) {
            Operation(0L) { navController.navigateUp() }
        }
        composable(
            route = Screen.Operation.route + "/{operationId}",
            arguments = listOf(
                navArgument("operationId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            entry.arguments?.getLong("operationId")?.let {
                Operation(it) { navController.navigateUp() }
            }
        }
        composable(
            route = Screen.Movement.route + "/{movementId}",
            arguments = listOf(
                navArgument("movementId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            entry.arguments?.getLong("movementId")?.let {
                Movement(it) { navController.navigateUp() }
            }
        }
    }
}

fun NavGraphBuilder.categoriesGraph(
    navController: NavHostController) {
    navigation(
        startDestination = Screen.Categories.route,
        route = Screen.CategoriesRoute.route
    ) {
        composable(Screen.Categories.route) {
            CategoriesList { navController.navigate(Screen.Category.route + "/${it}") }
        }
        composable(
            route = Screen.Category.route + "/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            entry.arguments?.getLong("categoryId")?.let {
                Category(it) { navController.navigateUp() }
            }
        }
    }
}

fun NavGraphBuilder.categoryTypesGraph(
    navController: NavHostController) {
    navigation(
        startDestination = Screen.CategoryTypes.route,
        route = Screen.CategoryTypesRoute.route
    ) {
        composable(Screen.CategoryTypes.route) {
            CategoryTypesList { navController.navigate(Screen.CategoryType.route + "/${it}") }
        }
        composable(
            route = Screen.CategoryType.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            entry.arguments?.getLong("id")?.let {
                CategoryType(it) { navController.navigateUp() }
            }
        }
    }
}

fun NavGraphBuilder.accountsGraph(
    navController: NavHostController) {
    navigation(
        startDestination = Screen.Accounts.route,
        route = Screen.AccountsRoute.route
    ) {
        composable(Screen.Accounts.route) {
            AccountsList { navController.navigate(Screen.Account.route + "/${it}") }
        }
        composable(
            route = Screen.Account.route + "/{accountId}",
            arguments = listOf(
                navArgument("accountId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            entry.arguments?.getLong("accountId")?.let {
                Account(it) { navController.navigateUp() }
            }
        }
    }
}