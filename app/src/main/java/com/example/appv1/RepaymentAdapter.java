package com.example.appv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RepaymentAdapter extends BaseAdapter {
    private Context context;
    private JSONArray repaymentData;

    public RepaymentAdapter(Context context, JSONArray repaymentData) {
        this.context = context;
        this.repaymentData = repaymentData;
    }

    public void setRepaymentData(JSONArray newRepaymentData) {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return repaymentData.length();
    }

    @Override
    public JSONObject getItem(int position) {
        try {
            return repaymentData.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.repayment_card, parent, false);
        }

        TextView repaymentDateTextView = convertView.findViewById(R.id.textView6);
        TextView actualRepayDate = convertView.findViewById(R.id.textView10);
        TextView balanceTextView = convertView.findViewById(R.id.textView24);
        TextView actualRepayAmount = convertView.findViewById(R.id.textView11);
        TextView emi = convertView.findViewById(R.id.textView7);
        TextView paid = convertView.findViewById(R.id.textView12);
        JSONObject item = getItem(position);

        if (item != null) {
            try {
                String repaymentDate = item.getString("repaymentDate");
                String actualRepaymentDate = item.getString("paymentDate");
                int balance = item.getInt("balance");
                boolean completed = item.getBoolean("completed");
                int paidAmount = item.getInt("paidAmount");
                int emiInt = item.getInt("emi");

                repaymentDateTextView.setText(repaymentDate);
                balanceTextView.setText("Balance: ₹" + balance);
                emi.setText("₹" + emiInt);

                if (completed) {
                    paid.setText("Paid");
                    if (paidAmount == 0) {
                        actualRepayAmount.setText("-");
                    } else {
                        actualRepayAmount.setText("₹" + paidAmount);
                    }
                } else {
                    paid.setText("Unpaid");
                    actualRepayAmount.setText("-");
                }

                if (actualRepaymentDate.isEmpty()) {
                    actualRepayDate.setText("-");
                } else {
                    actualRepayDate.setText(repaymentDate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

}
