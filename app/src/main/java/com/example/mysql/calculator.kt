package com.example.mysql

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.*

class calculator : AppCompatActivity() {

    private lateinit var tvDisplay: TextInputEditText
    private var currentInput: String = ""
    private var operator: String? = null
    private var operand1: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        tvDisplay = findViewById(R.id.tvDisplay)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnEquals, R.id.btnClear, R.id.btnSin, R.id.btnCos, R.id.btnTan,
            R.id.btnLog, R.id.btnLn, R.id.btnSqrt
        )

        buttons.forEach { id ->
            findViewById<MaterialButton>(id).setOnClickListener { onButtonClick(it as MaterialButton) }
        }
    }

    private fun onButtonClick(button: MaterialButton) {
        when (button.text.toString()) {
            "C" -> clear()
            "=" -> calculate()
            "+", "-", "*", "/" -> {
                setOperator(button.text.toString())
                tvDisplay.append(button.text) // Display operator
            }
            "sin", "cos", "tan", "log", "ln", "√" -> {
                calculateFunction(button.text.toString())
                tvDisplay.append(button.text) // Display function
            }
            else -> {
                appendNumber(button.text.toString())
                tvDisplay.append(button.text) // Display number
            }
        }
    }

    private fun appendNumber(number: String) {
        currentInput += number
    }

    private fun clear() {
        currentInput = ""
        operator = null
        operand1 = null
        tvDisplay.setText("")
    }

    private fun setOperator(op: String) {
        operator = op
        operand1 = currentInput.toDoubleOrNull()
        currentInput = ""
    }

    private fun calculate() {
        val operand2 = currentInput.toDoubleOrNull()
        if (operand1 != null && operand2 != null && operator != null) {
            val result = when (operator) {
                "+" -> operand1!! + operand2
                "-" -> operand1!! - operand2
                "*" -> operand1!! * operand2
                "/" -> operand1!! / operand2
                else -> null
            }
            tvDisplay.setText(result.toString())
            currentInput = result.toString()
        }
    }

    private fun calculateFunction(function: String) {
        val value = currentInput.toDoubleOrNull()
        if (value != null) {
            val result = when (function) {
                "sin" -> sin(Math.toRadians(value))
                "cos" -> cos(Math.toRadians(value))
                "tan" -> tan(Math.toRadians(value))
                "log" -> log10(value)
                "ln" -> ln(value)
                "√" -> sqrt(value)
                else -> null
            }
            tvDisplay.setText(result.toString())
            currentInput = result.toString()
        }
    }
}
