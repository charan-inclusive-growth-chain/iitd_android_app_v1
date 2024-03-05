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

public class LoanDetailApprovedActivity extends AppCompatActivity {

    private TextView loanIdTextView;
    private TextView appliedDateTextView;
    private TextView amountTextView;
    private TextView interestRateTextView;
    private Button viewDetailsButton;
    ListView repaymentListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_detail);

        loanIdTextView = findViewById(R.id.textView14);
        appliedDateTextView = findViewById(R.id.textView17);
        amountTextView = findViewById(R.id.textView19);
        interestRateTextView = findViewById(R.id.textView21);
        viewDetailsButton = findViewById(R.id.button);
        repaymentListView = findViewById(R.id.listview);


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

                JSONArray repaymentArray = loanObject.getJSONArray("farmerWindowRepaymentStructure");
                RepaymentAdapter repaymentAdapter = new RepaymentAdapter(this, repaymentArray);
                repaymentListView.setAdapter(repaymentAdapter);

                List<JSONObject> updatedRepaymentData = new ArrayList<>();

                for (int i = 0; i < repaymentArray.length(); i++) {
                    JSONObject repaymentItem = repaymentArray.getJSONObject(i);
                    updatedRepaymentData.add(repaymentItem);
                }
                repaymentAdapter.setRepaymentData(repaymentArray);
                repaymentAdapter.notifyDataSetChanged();



                viewDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoanDetailApprovedActivity.this, LoanApplicationDetailsActivity.class);
                        intent.putExtra("loanObject", loanObjectString);
                        startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
