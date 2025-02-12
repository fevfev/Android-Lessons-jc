package com.androidlesson.calculator.templates.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State
data class CalculatorState(
    val displayValue: String = "0",
    val error: String? = null,
    val firstNumber: Double? = null,
    val operation: String? = null,
    val newNumber: Boolean = true
)

// Intent
sealed class CalculatorIntent {
    data class NumberClicked(val number: Int) : CalculatorIntent()
    data class OperationClicked(val operation: String) : CalculatorIntent()
    object EqualsClicked : CalculatorIntent()
    object ClearClicked : CalculatorIntent()
}

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
class CalculatorViewModel {
    private val model = CalculatorModel()
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state

    fun processIntent(intent: CalculatorIntent) {
        when (intent) {
            is CalculatorIntent.NumberClicked -> handleNumberClick(intent.number)
            is CalculatorIntent.OperationClicked -> handleOperationClick(intent.operation)
            is CalculatorIntent.EqualsClicked -> handleEqualsClick()
            is CalculatorIntent.ClearClicked -> handleClearClick()
        }
    }

    private fun handleNumberClick(number: Int) {
        val currentState = _state.value
        val currentValue = currentState.displayValue
        val newValue = when {
            currentState.newNumber -> number.toString()
            currentValue == "0" -> number.toString()
            else -> currentValue + number
        }
        _state.value = currentState.copy(
            displayValue = newValue,
            newNumber = false,
            error = null
        )
    }

    private fun handleOperationClick(operation: String) {
        try {
            val currentState = _state.value
            _state.value = currentState.copy(
                firstNumber = currentState.displayValue.toDouble(),
                operation = operation,
                newNumber = true,
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = "Неверный ввод")
        }
    }

    private fun handleEqualsClick() {
        try {
            val currentState = _state.value
            currentState.operation?.let { operation ->
                currentState.firstNumber?.let { first ->
                    val second = currentState.displayValue.toDouble()
                    val result = model.calculate(first, second, operation)
                    _state.value = currentState.copy(
                        displayValue = result.toString(),
                        newNumber = true,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = "Ошибка вычисления")
        }
    }

    private fun handleClearClick() {
        _state.value = CalculatorState()
    }
} 