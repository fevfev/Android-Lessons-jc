package com.androidlesson.calculator.templates.mvp

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

// View interface
interface CalculatorView {
    fun showResult(result: String)
    fun showError(message: String)
    fun getCurrentInput(): String
}

// Presenter
class CalculatorPresenter(
    private val view: CalculatorView,
    private val model: CalculatorModel
) {
    private var firstNumber: Double? = null
    private var operation: String? = null

    fun onNumberClick(number: Int) {
        val currentInput = view.getCurrentInput()
        view.showResult(currentInput + number)
    }

    fun onOperationClick(op: String) {
        try {
            firstNumber = view.getCurrentInput().toDouble()
            operation = op
            view.showResult("")
        } catch (e: Exception) {
            view.showError("Неверный ввод")
        }
    }

    fun onEqualsClick() {
        try {
            val secondNumber = view.getCurrentInput().toDouble()
            operation?.let { op ->
                firstNumber?.let { first ->
                    val result = model.calculate(first, secondNumber, op)
                    view.showResult(result.toString())
                }
            }
        } catch (e: Exception) {
            view.showError("Ошибка вычисления")
        }
    }
} 