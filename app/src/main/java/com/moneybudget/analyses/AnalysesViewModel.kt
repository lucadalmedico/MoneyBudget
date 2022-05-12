package com.moneybudget.analyses

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.moneybudget.utils.DateUtils
import com.moneybudget.utils.dateToLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class AnalysesViewModel : ViewModel() {
    private val _startDate = MutableStateFlow(0L)
    val startDate: StateFlow<Long> = _startDate

    init {
        _startDate.value = DateUtils.getTodayDate().time
    }

    fun onStartDateChange(newDate: String) {
        _startDate.value = newDate.dateToLong()
    }

    private val colors by lazy {
        listOf(
            Color(android.graphics.Color.parseColor("#f44336")),
            Color(android.graphics.Color.parseColor("#5c6bc0")),
            Color(android.graphics.Color.parseColor("#4caf50")),
            Color(android.graphics.Color.parseColor("#ffca28")),
            Color(android.graphics.Color.parseColor("#e040fb")),
            Color(android.graphics.Color.parseColor("#ff6e40")),
            Color(android.graphics.Color.parseColor("#26c6da")),
            Color(android.graphics.Color.parseColor("#90a4ae")),
        )
    }

    fun getColorList(): List<Color> = colors

    fun getColor(index: Int): Color {
        return if (colors.size >= index)
            colors[index]
        else Color.Yellow
    }
}