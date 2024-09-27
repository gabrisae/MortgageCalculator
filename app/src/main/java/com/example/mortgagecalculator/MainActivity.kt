package com.example.mortgagecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.ui.theme.MortgageCalculatorTheme
import java.text.NumberFormat
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MortgageCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MortgageCalculatorScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorScreen(modifier: Modifier = Modifier) {
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var term by remember { mutableStateOf("") }
    var monthlyPayment by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = loanAmount,
            onValueChange = { loanAmount = it },
            label = { Text(stringResource(id = R.string.loan_amount)) },
            leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = "Loan Amount") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text(stringResource(id = R.string.interest_rate)) },
            leadingIcon = { Icon(Icons.Filled.Percent, contentDescription = "Interest Rate") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = term,
            onValueChange = { term = it },
            label = { Text(stringResource(id = R.string.number_of_years)) },
            leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = "Number of Years") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                monthlyPayment = calculateMortgage(loanAmount, interestRate, term)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Calculate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Monthly Payment: $monthlyPayment",
            fontSize = 20.sp
        )
    }
}

private fun calculateMortgage(p: String, r: String, n: String): String {
    val principal = p.toIntOrNull() ?: return "Invalid input"
    val rate = r.toDoubleOrNull()?.div(1200) ?: return "Invalid input" // Convert annual rate to monthly
    val months = n.toIntOrNull()?.times(12) ?: return "Invalid input" // Convert years to months

    var mortgage = principal * ((rate * (1.0 + rate).pow(months)) / ((1.0 + rate).pow(months) - 1))
    if (mortgage.isNaN() || mortgage.isInfinite()) mortgage = 0.0
    return NumberFormat.getCurrencyInstance().format(mortgage)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MortgageCalculatorTheme {
        MortgageCalculatorScreen()
    }
}
