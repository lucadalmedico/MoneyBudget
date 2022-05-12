package com.moneybudget.ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.moneybudget.R
import com.moneybudget.utils.Screen
import com.moneybudget.utils.viewModels.Filters
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListElement(
    onCardClick: () -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    onCardSwipe: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onCardSwipe()
            }
            it != DismissValue.DismissedToStart
        }
    )

    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.1f) },
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.DismissedToStart -> Color.Red
                    else -> {
                        MaterialTheme.colors.surface
                    }
                }
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            Card(
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onCardClick() }),
                elevation = 2.dp,
            ) {
                Row(
                    Modifier
                        .padding(PaddingValues(4.dp, 16.dp, 4.dp, 16.dp)),
                    horizontalArrangement = horizontalArrangement,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    )
}

@Composable
fun LazyColumnList(content: LazyListScope.() -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(4.dp),
        content = content
    )
}

@Composable
fun FilterButton(
    text: String,
    onClick: () -> Unit,
    isActiveFilter: Boolean
) {

    OutlinedButton(
        onClick = { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor =
            if (isActiveFilter)
                MaterialTheme.colors.secondaryVariant
            else
                MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        shape = RoundedCornerShape(40.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun AmountText(
    amount: Float,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.body2
) {
    val amountFormatted = String.format("%.2f", amount)
    Text(
        text = "$amountFormatted €",
        style = style,
        color = if (amount >= 0) MaterialTheme.colors.onSurface else Color.Red
    )
}

@Composable
fun AmountOnTotalText(
    amount: Float,
    total: Float,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.body2
) {
    val amountFormatted = String.format("%.2f", amount)
    val totalFormatted = String.format("%.2f", total)

    Text(
        text = "$amountFormatted € / $totalFormatted €",
        style = style,
        color = if (amount <= total) MaterialTheme.colors.onSurface else Color.Red
    )
}


@Composable
fun FiltersRow(
    listOfFilters: List<Filters>
) {
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(listOfFilters) { filters ->
            filters.filtersList.forEach { filter ->
                FilterButton(
                    filter.name,
                    onClick = {
                        filters.onFilterClick(filter.id)
                    },
                    filters.filterSelected == filter.id
                )
            }
        }
    }
}

@Composable
fun ListComposable(
    list : List<*>?,
    addButtonOnClick : () -> Unit,
    onElementClick : (Any) -> Unit = {},
    elementHorizontalArrangement : Arrangement.Horizontal = Arrangement.SpaceBetween,
    viewModel : ListViewModel<*>,
    listOfFilters: List<Filters> = emptyList(),
    rowContent : @Composable RowScope.(Any) -> Unit = {}) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val snackBarActionMessage = stringResource(id = R.string.undo)
    val snackBarMessage = stringResource(id = R.string.deleted)

    var showFloatingActionButton by remember { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                showFloatingActionButton = available.y > 0
                return Offset.Zero
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection),
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if(showFloatingActionButton) {
                FloatingActionButton(
                    onClick = { addButtonOnClick() }) {
                    Icon(Icons.Default.Add, stringResource(id = R.string.add))
                }
            }
        },
        isFloatingActionButtonDocked = true) {
        LazyColumnList{
            if(!listOfFilters.isNullOrEmpty()) {
                item {
                    FiltersRow(listOfFilters)
                }
            }
            if(list == null) {
                item {
                    CircularProgressIndicator()
                }
            }
            else {
                items(list) { item ->
                    if(item != null) {
                        ListElement(
                            onCardClick = { onElementClick(item) },
                            horizontalArrangement = elementHorizontalArrangement,
                            onCardSwipe = {
                                coroutineScope.launch {
                                    viewModel.updateDeletedFlag(item, true)

                                    val snackResult = scaffoldState
                                        .snackbarHostState
                                        .showSnackbar(
                                            snackBarMessage,
                                            snackBarActionMessage
                                        )

                                    when (snackResult) {
                                        SnackbarResult.Dismissed -> {
                                            viewModel.deleteElement(item)
                                        }
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.updateDeletedFlag(item, false)
                                        }
                                    }
                                }
                            }
                        ) {
                            rowContent(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabsColumn(
    tabsContent : List<Pair<Int, Screen>>,
    navController : NavHostController,
    content : @Composable (ColumnScope.() -> Unit)) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var selectedTabIndexState by remember { mutableStateOf(0) }

    val selectedRoute = tabsContent.map { it.second.route }
        .indexOf(currentDestination?.route)

    if(selectedRoute > 0) selectedTabIndexState = selectedRoute

    Column {
        TabRow(selectedTabIndex = selectedTabIndexState) {
            tabsContent.forEachIndexed { index, (title, _) ->
                Tab(
                    text = { Text(stringResource(id = title)) },
                    selected = selectedTabIndexState == index,
                    onClick = {
                        selectedTabIndexState = index
                        navController.navigate(tabsContent[index].second.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

        content()
    }
}