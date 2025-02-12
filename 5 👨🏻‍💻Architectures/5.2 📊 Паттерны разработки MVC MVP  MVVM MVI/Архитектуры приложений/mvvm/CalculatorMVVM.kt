package com.androidlesson.calculator.templates.mvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Model
class CalculatorModel {
    fun calculate(a: Double, b: Double, operation: String): Double {
        return when (operation) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b
            else -> throw IllegalArgumentException("Unknown operation")
        }
    }
}

// ViewModel
class CalculatorViewModel : ViewModel() {
    private val model = CalculatorModel()

    private val _displayValue = MutableStateFlow("0")
    val displayValue = _displayValue.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var firstNumber: Double? = null
    private var operation: String? = null
    private var newNumber = true

    fun onNumberClick(number: Int) {
        val currentValue = _displayValue.value
        _displayValue.value = when {
            newNumber -> number.toString()
            currentValue == "0" -> number.toString()
            else -> currentValue + number
        }
        newNumber = false
    }

    fun onOperationClick(op: String) {
        try {
            firstNumber = _displayValue.value.toDouble()
            operation = op
            newNumber = true
        } catch (e: Exception) {
            _error.value = "Неверный ввод"
        }
    }

    fun onEqualsClick() {
        try {
            val secondNumber = _displayValue.value.toDouble()
            operation?.let { op ->
                firstNumber?.let { first ->
                    val result = model.calculate(first, secondNumber, op)
                    _displayValue.value = result.toString()
                    newNumber = true
                }
            }
        } catch (e: Exception) {
            _error.value = "Ошибка вычисления"
        }
    }
} 