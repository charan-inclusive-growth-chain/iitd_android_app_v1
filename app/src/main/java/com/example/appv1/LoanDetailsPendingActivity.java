package com.example.appv1;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoanDetailsPendingActivity extends AppCompatActivity {
    private TextView loanIdTextView;
    private TextView appliedDateTextView;
    private TextView amountTextView;
    private TextView interestRateTextView;
    private Button viewDetailsButton;
    private TextView statusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_loan_details_pending_and_rejected);

        loanIdTextView = findViewById(R.id.textView14);
        appliedDateTextView = findViewById(R.id.textView17);
        amountTextView = findViewById(R.id.textView19);
        interestRateTextView = findViewById(R.id.textView21);
        viewDetailsButton = findViewById(R.id.button);
        statusTextView = findViewById(R.id.textView4);




        Intent intent = getIntent();
        if (intent.hasExtra("loanObject")) {
            try {
                String loanObjectString = intent.getStringExtra("loanObject");
                Log.d("Loan Object", loanObjectString);
                JSONObject loanObject = new JSONObject(loanObjectString);

                String loanId = loanObject.getString("loanId");
                String appliedDate = loanObject.getString("createdAt").substring(0, 10);
                String amount = "₹" + loanObject.getString("requestedAmount");
                String interestRate = loanObject.getString("fpointrest") + "%";

                loanIdTextView.setText(loanId);
                appliedDateTextView.setText(appliedDate);
                amountTextView.setText(amount);
                interestRateTextView.setText(interestRate);

                viewDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoanDetailsPendingActivity.this, LoanApplicationDetailsActivity.class);
                        intent.putExtra("loanObject", loanObjectString);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(intent.hasExtra("pendingStatus")) {
            String pendingStatus = intent.getStringExtra("pendingStatus");
            statusTextView.setText(pendingStatus);
        }
    }


}
