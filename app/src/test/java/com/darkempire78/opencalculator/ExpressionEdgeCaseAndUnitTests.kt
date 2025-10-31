package com.darkempire78.opencalculator

import com.darkempire78.opencalculator.calculator.Calculator
import com.darkempire78.opencalculator.calculator.parser.Expression
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.text.DecimalFormatSymbols

/**
 * Edge case tests for calculator expressions
 */
class ExpressionEdgeCaseAndUnitTests {

    private val decimalSeparatorSymbol = DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    private val groupingSeparatorSymbol = DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    @Test
    fun very_large_numbers_areHandled() {
        val result = calculate("999999999*999999999", false).toDouble()
        assertEquals(9.99999998E17, result, 1E10)
    }

    @Test
    fun very_small_numbers_areHandled() {
        var result = calculate("0.000001*0.000001", false).toDouble()
        assertEquals(1E-12, result, 1E-15)

        result = calculate("1/1000000", false).toDouble()
        assertEquals(0.000001, result, 0.0)
    }

    @Test
    fun negative_numbers_in_expressions_isCorrect() {
        var result = calculate("-5+3", false).toDouble()
        assertEquals(-2.0, result, 0.0)

        result = calculate("-5*-3", false).toDouble()
        assertEquals(15.0, result, 0.0)

        result = calculate("(-5)^2", false).toDouble()
        assertEquals(25.0, result, 0.0)

        result = calculate("√(-5+14)", false).toDouble()
        assertEquals(3.0, result, 0.0)
    }

    @Test
    fun sqrt_of_perfect_squares_isCorrect() {
        var result = calculate("√1", false).toDouble()
        assertEquals(1.0, result, 0.0)

        result = calculate("√4", false).toDouble()
        assertEquals(2.0, result, 0.0)

        result = calculate("√25", false).toDouble()
        assertEquals(5.0, result, 0.0)

        result = calculate("√100", false).toDouble()
        assertEquals(10.0, result, 0.0)

        result = calculate("√144", false).toDouble()
        assertEquals(12.0, result, 0.0)
    }

    @Test
    fun sqrt_of_decimal_numbers_isCorrect() {
        var result = calculate("√0.25", false).toDouble()
        assertEquals(0.5, result, 0.0000001)

        result = calculate("√0.0625", false).toDouble()
        assertEquals(0.25, result, 0.0000001)

        result = calculate("√2.25", false).toDouble()
        assertEquals(1.5, result, 0.0000001)
    }

    @Test
    fun percentage_with_parentheses_isCorrect() {
        var result = calculate("(100+50)*20%", false).toDouble()
        assertEquals(30.0, result, 0.0)

        result = calculate("100+(50*20%)", false).toDouble()
        assertEquals(110.0, result, 0.0)

        result = calculate("(100*20%)+(50*10%)", false).toDouble()
        assertEquals(25.0, result, 0.0)
    }

    @Test
    fun extreme_factorial_boundary_isCorrect() {
        // Test factorial of 0
        var result = calculate("0!", false).toDouble()
        assertEquals(1.0, result, 0.0)

        // Test factorial of 1
        result = calculate("1!", false).toDouble()
        assertEquals(1.0, result, 0.0)

        // Test factorial of 2
        result = calculate("2!", false).toDouble()
        assertEquals(2.0, result, 0.0)

        // Test larger factorials
        result = calculate("12!", false).toDouble()
        assertEquals(479001600.0, result, 0.0)
    }

    @Test
    fun pi_isCorrect() {
        var result = calculate("π", false).toDouble()
        assertEquals(3.1415926535, result, 0.0000000001)

        result = calculate("5π", false).toDouble()
        assertEquals(15.7079632679, result, 0.0000000001)

        result = calculate("5*π", false).toDouble()
        assertEquals(15.7079632679, result, 0.0000000001)

        result = calculate("2+π", false).toDouble()
        assertEquals(5.14159265359, result, 0.0000000001)

        result = calculate("5-π", false).toDouble()
        assertEquals(1.85840734641, result, 0.0000000001)

        result = calculate("1/π", false).toDouble()
        assertEquals(0.31830988618, result, 0.0000000001)
    }

    @Test
    fun euler_isCorrect() {
        var result = calculate("e", false).toDouble()
        assertEquals(2.71828182846, result, 0.0000000001)

        result = calculate("2e", false).toDouble()
        assertEquals(5.43656365692, result, 0.0000000001)

        result = calculate("2*e", false).toDouble()
        assertEquals(5.43656365692, result, 0.0000000001)

        result = calculate("2+e", false).toDouble()
        assertEquals(4.71828182846, result, 0.0000000001)

        result = calculate("5-e", false).toDouble()
        assertEquals(2.28171817154, result, 0.0000000001)

        result = calculate("1/e", false).toDouble()
        assertEquals(0.36787944117, result, 0.0000000001)
    }

    @Test
    fun logarithms_areCorrect() {
        var result = calculate("log(10)", false).toDouble()
        assertEquals(1.0, result, 0.0)

        result = calculate("log(15)", false).toDouble()
        assertEquals(1.17609125906, result, 0.0000000001)

        result = calculate("ln(e)", false).toDouble()
        assertEquals(1.0, result, 0.0)

        result = calculate("ln(15)", false).toDouble()
        assertEquals(2.7080502011, result, 0.0000000001)
    }

    private fun calculate(input: String, isDegreeModeActivated: Boolean) =
        calculator.evaluate(
            expression.getCleanExpression(input, decimalSeparatorSymbol, groupingSeparatorSymbol),
            isDegreeModeActivated
        )

    companion object {
        private lateinit var expression: Expression
        private lateinit var calculator: Calculator

        @BeforeClass
        @JvmStatic
        fun setup() {
            expression = Expression()
            calculator = Calculator(10)
        }
    }
}