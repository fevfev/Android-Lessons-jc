package com.androidlesson.calculator.templates.mvc

// Model
class CalculatorModel {
    fun add(a: Double, b: Double) = a + b
    fun subtract(a: Double, b: Double) = a - b
    fun multiply(a: Double, b: Double) = a * b
    fun divide(a: Double, b: Double) = a / b
}

// Controller
class CalculatorController(
    private val model: CalculatorModel,
    private val view: CalculatorView
) {
    fun onNumberClick(number: Int) {
        view.updateDisplay(number.toString())
    }

    fun onOperationClick(operation: String) {
        // Логика обработки операции
    }

    fun onEqualsClick() {
        // Логика вычисления результата
    }
}

// View
interface CalculatorView {
    fun updateDisplay(value: String)
    fun showError(message: String)
} 