package com.moneybudget.operations.operationsList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moneybudget.R
import com.moneybudget.categories.CategoryName
import com.moneybudget.database.OperationMinimal
import com.moneybudget.operations.movementsList.MovementsList
import com.moneybudget.ui.composable.AmountText
import com.moneybudget.ui.composable.ListComposable
import com.moneybudget.ui.composable.TabsColumn
import com.moneybudget.utils.Screen
import com.moneybudget.utils.formatDate
import com.moneybudget.utils.viewModels.Filters
import com.moneybudget.utils.viewModels.ListViewModel

@Composable
fun OperationsAndMovementsList(mainNavController: NavHostController) {
    val navController = rememberNavController()

    val tabsContent = listOf(
        Pair(R.string.operations , Screen.Operations),
        Pair(R.string.movements, Screen.Movement)
    )

    TabsColumn(
        tabsContent = tabsContent,
        navController = navController
    ){
        NavHost(navController, startDestination = Screen.Operations.route) {
            composable(Screen.Operations.route) {
                OperationsList {
                    mainNavController.navigate(Screen.Operation.route + "/${it}") }
            }
            composable(Screen.Movement.route) {
                MovementsList { mainNavController.navigate(Screen.Movement.route + "/${it}") }
            }
        }
    }
}

@Composable
fun OperationsList(onElementClick: (Long) -> Unit) {
    val viewModel: OperationsListViewModel = viewModel()

    val operationsList by viewModel.operationsList.collectAsState(initial = null)
    val categoryIdFiltered by viewModel.categoryIdFiltered.collectAsState()
    val accountIdFiltered by viewModel.accountIdFiltered.collectAsState()
    val accountsList by viewModel.getAccountsList().collectAsState(null)
    val categoriesList by viewModel.getCategoriesList().collectAsState(null)

    val listOfFilters = listOf(
        Filters(
            accountsList ?: emptyList(),
            { viewModel.setAccountIdFiltered(it) },
            accountIdFiltered
        ),
        Filters(
            categoriesList ?: emptyList(),
            { viewModel.setCategoryIdFiltered(it) },
            categoryIdFiltered
        )
    )

    OperationsListView(
        operationsList,
        onAddButtonOnClick = { onElementClick(0L) },
        onElementClick = { onElementClick(it.operationId) },
        viewModel = viewModel,
        listOfFilters = listOfFilters
        ){
        OperationsListElement(operation = it)
    }
}

@Composable
fun OperationsListView(
    operationsList : List<OperationMinimal>?,
    onAddButtonOnClick : () -> Unit,
    onElementClick : (OperationMinimal) -> Unit,
    viewModel: ListViewModel<*>,
    listOfFilters: List<Filters> = emptyList(),
    elementComposable : @Composable RowScope.(OperationMinimal) -> Unit
){
    ListComposable(
        list = operationsList,
        addButtonOnClick = { onAddButtonOnClick() },
        onElementClick = { onElementClick(it as OperationMinimal) },
        viewModel = viewModel,
        listOfFilters = listOfFilters) {

        elementComposable(it as OperationMinimal)
    }
}

@Composable
fun OperationsListElement(
    operation: OperationMinimal
) {
    Column {
        Text(
            text = operation.operationDate.formatDate(),
            style = MaterialTheme.typography.body2,
        )
        CategoryName(
            id = operation.categoryId?:0L,
            textStyle = MaterialTheme.typography.caption)
    }

    Column {
        AmountText(amount = operation.amount ?: 0f)
    }
}