package com.jetbrains.greeting

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            App()
            CalcView()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Preview
@Composable
fun CalcView() {
    val displayText = rememberSaveable {
        mutableStateOf("0")
    }

    val leftNumber = rememberSaveable {
        mutableIntStateOf(0)
    }

    val rightNumber = rememberSaveable {
        mutableIntStateOf(0)
    }

    val operation = rememberSaveable {
        mutableStateOf("")
    }

    val complete = rememberSaveable {
     mutableStateOf(false)
    }

    if(complete.value && operation.value.isNotEmpty()){
        val answer = rememberSaveable {
            mutableIntStateOf(0)
        }
        when (operation.value) {
            "+" -> answer.intValue = leftNumber.intValue + rightNumber.intValue
            "-" -> answer.intValue = leftNumber.intValue - rightNumber.intValue
            "*" -> answer.intValue = leftNumber.intValue * rightNumber.intValue
            "/" -> {
                if (rightNumber.intValue != 0) {
                    answer.intValue = leftNumber.intValue / rightNumber.intValue
                } else {
                    displayText.value = "Error, cannot divide by zero"
                    return
                }
            }
            else -> displayText.value = "Error no operation detected"
        }
        displayText.value = answer.intValue.toString()
    }else if(!complete.value &&operation.value.isNotEmpty()){
         displayText.value = rightNumber.intValue.toString()
    }else{
        displayText.value = leftNumber.intValue.toString()
    }

    fun numberPress(btnNum: Int) {
        if (complete.value) {
            leftNumber.intValue = 0
            rightNumber.intValue = 0
            operation.value = ""
            complete.value = false
        }

        if (operation.value.isNotBlank() && !complete.value) {
            rightNumber.intValue = rightNumber.intValue * 10 + btnNum
        }

        if (operation.value.isBlank() && !complete.value) {
            leftNumber.intValue = leftNumber.intValue * 10 + btnNum
        }
    }

    fun operationPress(op: String) {
        if (!complete.value) {
            operation.value = op
        }
    }

    fun equalsPress() {
        complete.value = true
    }

    Column(
        Modifier.background(Color.LightGray)
    ) {
        Row {
            CalcDisplay(displayText)
        }
        Row {
            Column {
                for (number in 7 downTo 1 step 3) {
                    CalcRow({ number -> numberPress(number) }, number, 3)
                }
                Row {
                    CalcNumericButton(0) { number -> numberPress(number) }
                    CalcEqualsButton { equalsPress() }
                }
            }
            Column {
                CalcOperationButton("+") { op -> operationPress(op) }
                CalcOperationButton("-") { op -> operationPress(op) }
                CalcOperationButton("*") { op -> operationPress(op) }
                CalcOperationButton("/") { op -> operationPress(op) }
            }
        }
    }
}

@Composable
fun CalcRow(onPress: (number: Int) -> Unit, startNum: Int, numButtons: Int) {
    val endNum = startNum + numButtons
    Row(
        modifier = Modifier.padding(0.dp)
    ) {
        for (number in startNum..<endNum) {
            CalcNumericButton(number, onPress)
        }
    }
}

@Composable
fun CalcDisplay(display: MutableState<String>) {
    Text(
        display.value,
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp)
            .fillMaxWidth()
    )
}

@Composable
fun CalcNumericButton(number: Int, onPress: (number: Int) -> Unit) {
    Button(
        onClick = { onPress(number) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(number.toString())
    }
}

@Composable
fun CalcOperationButton(operation: String, onPress: (operation: String) -> Unit) {
    Button(
        onClick = { onPress(operation) },
        modifier = Modifier.padding(4.dp)
    ) {
        Text(operation)
    }
}

@Composable
fun CalcEqualsButton(onPress: () -> Unit) {
    Button(
        onClick = { onPress() },
        modifier = Modifier.padding(4.dp)
    ) {
        Text("=")
    }
}