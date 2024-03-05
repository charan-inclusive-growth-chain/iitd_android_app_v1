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

import androidx.annotation.NonNull;
import com.example.appv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoanPendingAdapter extends ArrayAdapter<JSONObject> {
    private final List<JSONObject> loanList;
    public LoanPendingAdapter(@NonNull Context context, List<JSONObject> loanList) {
        super(context, R.layout.loan_pending_item, loanList);
        this.loanList = loanList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.loan_pending_item, null);
        }

        // Parse JSON data from loanList and populate your loan_pending_item.xml layout here
        JSONObject loanObject = loanList.get(position);
        try {
            String loanAppDate = loanObject.getString("createdAt").substring(0, 10);
            String loanAmount = loanObject.getString("requestedAmount");
            String loanInterestRate = loanObject.getString("fpointrest") + "%";
            String windowId = loanObject.getString("loanWindowId");
            String loanId = loanObject.getString("loanId");
            String pendingStatus = loanObject.getString("fpoApprovalStatus");

            if(pendingStatus.equals("in-process")) {
                pendingStatus = "Waiting for FPO Approval.";
            }
            else {
                pendingStatus = "Waiting for Samunnati Approval.";
            }



            // Populate the TextViews in your loan_approved_item layout
            TextView loanDateTextView = convertView.findViewById(R.id.loan_date);
            TextView loanIdTextView = convertView.findViewById(R.id.loan_id);
            TextView loanWindowIdTextView = convertView.findViewById(R.id.loan_window_id);
            TextView pendingStatusTextView = convertView.findViewById(R.id.status);
            ImageButton info = convertView.findViewById(R.id.infoButton);
            String finalPendingStatus = pendingStatus;
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject currentJsonObject = loanList.get(position);
                    Intent intent = new Intent(getContext(), LoanDetailsPendingActivity.class);
                    intent.putExtra("loanObject", currentJsonObject.toString());
                    intent.putExtra("pendingStatus", finalPendingStatus);
                    getContext().startActivity(intent);
                }
            });

            //loanIdTextView.setText(loanId);
            loanDateTextView.setText(loanAppDate);
            loanIdTextView.setText(loanId);
            loanWindowIdTextView.setText(windowId);
            pendingStatusTextView.setText(pendingStatus);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }


}
