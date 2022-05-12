package com.moneybudget.ui.composable

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.moneybudget.R
import com.moneybudget.database.NameAndId
import com.moneybudget.utils.formatDate
import com.moneybudget.utils.viewModels.ElementViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun defaultKeyboardActions(): KeyboardActions {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    return KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            focusManager.clearFocus()
            keyboardController?.hide()
        })
}

@Composable
fun defaultKeyboardOptions(isLast: Boolean = false): KeyboardOptions {
    return KeyboardOptions(imeAction = if (isLast) ImeAction.Done else ImeAction.Next)
}

@Composable
fun Date(
    date: Long?,
    dateLabel: String = stringResource(id = R.string.date),
    isLast: Boolean = false,
    onDateChange: (String) -> Unit
) {
    val context = LocalContext.current

    if(date != null)
        OutlinedTextField(
            value = date.formatDate(),
            onValueChange = onDateChange,
            label = { Text(dateLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        val datePicker = DatePickerDialog(context)

                        datePicker.setOnDateSetListener(
                            DatePickerDialog.OnDateSetListener(
                                @Override
                                fun(_: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                                    val pickedDate = android.icu.util.Calendar.getInstance()
                                    pickedDate.set(year, month, dayOfMonth)
                                    onDateChange(pickedDate.time.time.formatDate())
                                })
                        )

                        datePicker.show()
                    }
                },
            readOnly = true,
            keyboardActions = defaultKeyboardActions(),
            keyboardOptions = defaultKeyboardOptions(isLast)
        )


}


@Composable
fun Amount(amount: String?,
           isLast: Boolean = false,
           onAmountChange: (String) -> Unit) {
    OutlinedTextField(
        value = amount ?: "",
        onValueChange = onAmountChange,
        label = { Text(stringResource(id = R.string.amount)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.EuroSymbol,
                contentDescription = null
            )
        },
        keyboardActions = defaultKeyboardActions()
    )
}

@Composable
fun Percent(percent: String?,
            isLast: Boolean = false,
            onPercentChange: (String) -> Unit) {
    var currentPercent = percent
    if (currentPercent != null && currentPercent != "") {
        if (currentPercent.toInt() > 100) {
            currentPercent = "100"
        } else if (currentPercent.toInt() < 0) {
            currentPercent = "0"
        }
    }

    OutlinedTextField(
        value = "$currentPercent",
        onValueChange = onPercentChange,
        label = { Text(stringResource(id = R.string.budgetPercentage)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = defaultKeyboardActions()
    )
}


@Composable
fun Name(name: String?,
         isLast: Boolean = false,
         onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = name ?: "",
        onValueChange = onNameChange,
        label = { Text(stringResource(id = R.string.name)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = defaultKeyboardActions(),
        keyboardOptions = defaultKeyboardOptions(isLast)
    )
}


@Composable
fun Description(
    description: String?,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = description ?: "",
        onValueChange = onDescriptionChange,
        label = { Text(stringResource(id = R.string.description)) },
        modifier = Modifier
            .defaultMinSize(20.dp, 20.dp)
            .fillMaxWidth(),
        keyboardActions = defaultKeyboardActions()
    )
}


@Composable
fun NamesDropDownMenu(
    list: List<NameAndId>?,
    nameSelected: String?,
    label: String,
    onIdChange: (Long) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Row(Modifier.clickable { // Anchor view
        expanded = !expanded
    }) {
        OutlinedTextField(
            value = nameSelected ?: "",
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier
                .onFocusChanged { expanded = it.isFocused }
                .fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown,
                    null,
                    Modifier.clickable { expanded = !expanded })
            },
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list?.forEach { element ->
                DropdownMenuItem(
                    onClick = {
                        onIdChange(element.id)
                        expanded = false
                    }) {
                    Text(text = element.name)
                }
            }
        }
    }
}


@Composable
fun ElementPage(
    elementId : Long,
    viewModel : ElementViewModel<*>,
    onBackPressed : () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    viewModel.initializeFromId(elementId)

    LocalOnBackPressedDispatcherOwner.current?.let {
        BackHandler(backDispatcher = it.onBackPressedDispatcher,
            onBack = {
                viewModel.saveElement()
                onBackPressed()
            },
            isEnabled = true)
    }

    Column(
        Modifier.padding(4.dp),
        content = content)
}

@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher,
    onBack: () -> Unit,
    isEnabled : Boolean = false) {

    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)


    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        // Always intercept back events. See the SideEffect for
        // a more complete version
        object : OnBackPressedCallback(isEnabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = isEnabled
    }

    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(backCallback)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}
