package dev.lab.appcalculatebill;
import dev.lab.appcalculatebill.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBill, btnReset;
    EditText editTextUnits, editTextRebate;
    TextView textViewTotalCharges, textViewFinalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBill = findViewById(R.id.btnBill);
        btnReset = findViewById(R.id.btnReset);

        editTextUnits = findViewById(R.id.editTextUnit);
        editTextRebate = findViewById(R.id.editTextRebate);
        textViewTotalCharges = findViewById(R.id.textViewTotalCharges);
        textViewFinalCost = findViewById(R.id.textViewFinalCost);

        btnBill.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ConstraintLayout constraintLayout = findViewById(R.id.main);

        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuInstruction) {
            Intent intent = new Intent(MainActivity.this, Instruction.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

            @Override
            public void onClick(View view) {
                if (view == btnBill) {
                    // Check for empty fields
                    if (editTextUnits.getText().toString().isEmpty()) {
                        editTextUnits.setError("Enter value");
                    } else if (editTextRebate.getText().toString().isEmpty()) {
                        editTextRebate.setError("Enter value of percentage");
                    } else {
                        double rebatePercentage = Double.parseDouble(editTextRebate.getText().toString());
                        if (rebatePercentage < 0 || rebatePercentage > 5) {
                            showInvalidRebateDialog();
                        } else {
                            calculateBill();
                            Toast.makeText(MainActivity.this, "Calculating", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (view == btnReset) {
                    showResetConfirmationDialog();
                }
            }

                private void showResetConfirmationDialog() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Reset Confirmation");
                    builder.setMessage("Are you sure you want to reset?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Reset logic here
                            editTextUnits.setText("");
                            editTextRebate.setText("");
                            textViewTotalCharges.setText("Total Charges:");
                            textViewFinalCost.setText("Final Cost:");
                            Toast.makeText(MainActivity.this, "Values reset", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            private void showInvalidRebateDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Invalid Rebate Percentage");
                builder.setMessage("Rebate percentage must be between 0% and 5%.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }


            private void calculateBill() {
                int units = Integer.parseInt(editTextUnits.getText().toString());
                double rebate = Double.parseDouble(editTextRebate.getText().toString()) / 100;

                double totalCharges = 0.0;
                if (units <= 200) {
                    totalCharges = units * 0.218;
                } else if (units <= 300) {
                    totalCharges = (200 * 0.218) + ((units - 200) * 0.334);
                } else if (units <= 600) {
                    totalCharges = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
                } else {
                    totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
                }

                double finalCost = totalCharges - (totalCharges * rebate);

                textViewTotalCharges.setText("Total Charges: RM" + String.format("%.2f", totalCharges));
                textViewFinalCost.setText("Final Cost: RM" + String.format("%.2f", finalCost));
            }
        }