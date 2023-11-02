package com.example.appv1;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoanPendingAdapter extends ArrayAdapter<JSONObject> {
    private final List<JSONObject> loanList;

    public LoanPendingAdapter(Context context, List<JSONObject> loanList) {
        super(context, R.layout.loan_pending_item, loanList);
        this.loanList = loanList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.loan_approved_item, null);
        }

        // Parse JSON data from loanList and populate your loan_approved_item.xml layout here
        JSONObject loanObject = loanList.get(position);
        try {
            String LoanAppDate = loanObject.getString("createdAt").substring(0, 10);
            String LoanAmount = loanObject.getString("requestedAmount");
            String LoanInterestRate = loanObject.getString("intrest") + "%";
            String LoanDate = loanObject.getString("approvalAt").substring(0, 10);
            JSONArray repay = loanObject.getJSONArray("farmerWindowRepaymentStructure");
            String OutstandingAmount = "";
            String NextPaymentAmount = "";
            String NextPaymentDate = "";
            for (int j = 0; j < repay.length(); j++) {
                JSONObject bill = repay.getJSONObject(j);
                if (bill.getBoolean("completed") == false) {
                    OutstandingAmount = String.valueOf(bill.getInt("balance"));
                    NextPaymentAmount = "₹" + String.valueOf(bill.getInt("emi"));
                    NextPaymentDate = bill.getString("repaymentDate");
                    break;
                }
            }

            // Populate the TextViews in your loan_approved_item layout
            TextView loanIdTextView = convertView.findViewById(R.id.loan_app_date);
            TextView loanAppDateTextView = convertView.findViewById(R.id.loan_date);
            TextView loanAmountTextView = convertView.findViewById(R.id.total_amount);
            TextView paidAmountTextView = convertView.findViewById(R.id.paid_amount);
            TextView balanceAmountTextView = convertView.findViewById(R.id.balance_amount);
            TextView nextPaymentAmountTextView = convertView.findViewById(R.id.next_amount);
            ImageButton info = convertView.findViewById(R.id.infoButton);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject currentJsonObject = loanList.get(position);
                    Intent intent = new Intent(getContext(), LoanDetailApprovedActivity.class);
                    intent.putExtra("loanObject", currentJsonObject.toString());
                    getContext().startActivity(intent);
                }
            });

            //loanIdTextView.setText(loanId);
            loanAppDateTextView.setText(LoanAppDate);
            loanAmountTextView.setText("₹" + LoanAmount);
            paidAmountTextView.setText("₹" + String.valueOf(Integer.valueOf(LoanAmount) - Integer.valueOf(OutstandingAmount)));
            balanceAmountTextView.setText("₹" + OutstandingAmount);
            nextPaymentAmountTextView.setText(NextPaymentAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
