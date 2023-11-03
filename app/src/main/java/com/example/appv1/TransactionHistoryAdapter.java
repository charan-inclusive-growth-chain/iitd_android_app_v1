package com.example.appv1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TransactionHistoryAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private List<JSONObject> purchaseHistoryData;

    public TransactionHistoryAdapter(Context context, List<JSONObject> purchaseHistoryData) {
        super(context, R.layout.transaction_card, purchaseHistoryData);
        this.context = context;
        this.purchaseHistoryData = purchaseHistoryData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.transaction_card, parent, false);
        }

        TextView sno = convertView.findViewById(R.id.sno);
        TextView id = convertView.findViewById(R.id.id);
        TextView amount = convertView.findViewById(R.id.total_amount);
        AppCompatButton details = convertView.findViewById(R.id.details_button);
        JSONObject item = purchaseHistoryData.get(position);

        if (item != null) {
            try {
                String IdFull = item.getString("uniqueId");
                int length = IdFull.length();
                int startIndex = length / 2;
                String Id = IdFull.substring(startIndex+6);
                String Amount = item.getString("totalAmount");

                sno.setText(String.valueOf(position + 1)+ ". ");
                id.setText(R.string.id + ": " + Id);
                amount.setText("₹" + Amount);
                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle the button click, e.g., navigate to details screen
                        Intent intent = new Intent(context, TransactionDetailsActivity.class);
                        intent.putExtra("transaction_item", item.toString());
                        context.startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
