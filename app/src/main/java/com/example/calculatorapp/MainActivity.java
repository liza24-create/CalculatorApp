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
                String currentText = txtResult.getText().toString();

                if (currentText.isEmpty()) return;
                if (currentText.endsWith(" ")) {
                    return;
                }

                if (!currentOperation.isEmpty()) {
                    findViewById(R.id.btnEquals).performClick();
                    currentText = txtResult.getText().toString();
                    if (currentText.equals("Error")) return;
                }

                try {
                    double value = Double.parseDouble(currentText);
                    if (v.getId() == R.id.btnSqrt) {
                        if (value < 0) {
                            txtResult.setText("Error");
                        } else {
                            double result = Math.sqrt(value);
                            txtResult.setText(String.valueOf(result).replaceAll("\\.0$", ""));
                        }
                        return;
                    }

                    firstNumber = value;
                    currentOperation = opText;
                    txtResult.setText(String.format("%s %s ", currentText, opText));

                } catch (NumberFormatException e) {
                }
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

                String fullText = txtResult.getText().toString();
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

                String finalResult = String.valueOf(result).replaceAll("\\.0$", "");
                txtResult.setText(finalResult);
                currentOperation = "";
                isOpChosen = false;
            }
        });


        View.OnClickListener clearClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText("0");
                firstNumber = 0;
                currentOperation = "";
                isOpChosen = false;
            }
        };
        findViewById(R.id.btnC).setOnClickListener(clearClickListener);
        findViewById(R.id.btnCe).setOnClickListener(clearClickListener);


        findViewById(R.id.btnBackspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = txtResult.getText().toString();

                if (currentText.isEmpty() || currentText.equals("0") || currentText.equals("Error")) {
                    txtResult.setText("0");
                    return;
                }

                if (currentText.endsWith(" ")) {
                    currentText = currentText.substring(0, currentText.length() - 3);
                    currentOperation = "";
                    isOpChosen = false;
                } else {
                    currentText = currentText.substring(0, currentText.length() - 1);
                }

                if (currentText.isEmpty()) {
                    txtResult.setText("0");
                } else {
                    txtResult.setText(currentText);
                }
            }
        });

        findViewById(R.id.btnSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = txtResult.getText().toString();

                if (currentText.equals("0") || currentText.equals("Error")) return;


                if (currentOperation.isEmpty()) {
                    if (currentText.startsWith("-")) {
                        txtResult.setText(currentText.substring(1)); // remove minus
                    } else {
                        txtResult.setText(String.format("-%s", currentText));
                    }
                }
                else {
                    int opIndex = currentText.lastIndexOf(currentOperation) + currentOperation.length();
                    String firstPart = currentText.substring(0, opIndex).trim() + " ";
                    String secondPart = currentText.substring(opIndex).trim();

                    if (secondPart.isEmpty()) return;

                    if (secondPart.startsWith("-")) {
                        secondPart = secondPart.substring(1);
                    } else {
                        secondPart = "-" + secondPart;
                    }

                    txtResult.setText(String.format("%s%s", firstPart, secondPart));
                }
            }
        });

    }
}