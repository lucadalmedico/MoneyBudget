package com.moneybudget.operations.movementsList

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.accounts.AccountName
import com.moneybudget.database.MovementMinimal
import com.moneybudget.ui.composable.ListComposable

@Composable
fun MovementsList(onElementClick: (Long) -> Unit) {
    val viewModel: MovementsListViewModel = viewModel()

    val movementsList by viewModel.movementsList.collectAsState(initial = null)

    ListComposable(
        list = movementsList,
        addButtonOnClick = { onElementClick(0L) },
        onElementClick = { onElementClick((it as MovementMinimal).movementId) },
        viewModel = viewModel,
    ){
        MovementListElement(movement = it as MovementMinimal)
    }
}

@Composable
fun MovementListElement(movement : MovementMinimal) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AccountName(
            movement.sourceAccountId,
            MaterialTheme.typography.body1
        )

        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = null
        )

        AccountName(
            movement.destinationAccountId,
            MaterialTheme.typography.body2
        )
    }

    Column {
        Text(
            text = movement.amount.toString() + " €",
            style = MaterialTheme.typography.body1
        )
    }
}

