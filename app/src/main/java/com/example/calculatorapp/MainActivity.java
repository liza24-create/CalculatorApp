package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView txtResult;

    private double firstNumber = 0;
    private String currentOperation = "";
    private boolean isOpChosen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtResult);

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;

                String buttonText = clickedButton.getText().toString();
                String currentText = txtResult.getText().toString();

                if (buttonText.equals(",")) {
                    if (isOpChosen) {
                        String secondStr = currentText.substring(currentText.lastIndexOf(currentOperation) + currentOperation.length());
                        if (secondStr.contains(",")) return;
                    } else {
                        if (currentText.contains(",")) return;
                    }
                }
                if (currentText.equals("0") && !buttonText.equals(",")) {
                    txtResult.setText(buttonText);
                } else {
                    txtResult.setText(String.format("%s%s", currentText, buttonText));
                }
            }
        };

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnComma
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }

        View.OnClickListener opClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String opText = clickedButton.getText().toString();
                String currentText = txtResult.getText().toString().replace(",", ".");

                if (currentText.isEmpty()) return;
                double value = Double.parseDouble(currentText);

                if (v.getId() == R.id.btnSqrt) {
                    if (value < 0) {
                        txtResult.setText("Error");
                    } else {
                        double result = Math.sqrt(value);
                        txtResult.setText(String.valueOf(result).replaceAll("\\.0$", "").replace(".", ","));
                    }
                    isOpChosen = true;
                    return;
                }

                firstNumber = value;
                currentOperation = opText;
                isOpChosen = true;

                txtResult.setText(String.format("%s %s ", txtResult.getText().toString(), opText));

            }
        };

        int[] opIds = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide, R.id.btnSqrt};
        for (int id : opIds) {
            findViewById(id).setOnClickListener(opClickListener);
        }

        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOperation.isEmpty()) return;

                String fullText = txtResult.getText().toString().replace(",", ".");
                String secondStr = fullText.substring(fullText.lastIndexOf(currentOperation) + currentOperation.length()).trim();
                if (secondStr.isEmpty()) return;
                double secondNumber = Double.parseDouble(secondStr);
                double result = 0;

                switch (currentOperation) {
                    case "+": result = firstNumber + secondNumber; break;
                    case "-": result = firstNumber - secondNumber; break;
                    case "*": result = firstNumber * secondNumber; break;
                    case "÷":
                        if (secondNumber == 0) {
                            txtResult.setText("Error");
                            currentOperation = "";
                            isOpChosen = true;
                            return;
                        }
                        result = firstNumber / secondNumber;
                        break;
                }

                String finalResult = String.valueOf(result).replaceAll("\\.0$", "").replace(".", ",");
                txtResult.setText(finalResult);
                currentOperation = "";
                isOpChosen = false;
            }
        });

    }
}