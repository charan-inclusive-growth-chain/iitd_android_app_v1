package com.example.appv1;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appv1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class TransactionDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_detail);

        // Retrieve the JSON string from the intent
        String jsonItemString = getIntent().getStringExtra("transaction_item");

        try {
            // Parse the JSON item into a JSONObject
            JSONObject transactionItem = new JSONObject(jsonItemString);

            // Find the TableLayout in your layout
            TableLayout tableLayout = findViewById(R.id.transaction_table);

            // Get the "transactions" array from the JSON object
            JSONArray transactionsArray = transactionItem.getJSONArray("transactions");

            String totalAmount = transactionItem.getString("totalAmount");
            int total = 0;

            // Iterate through the transactions and create rows for each
            for (int i = 0; i < transactionsArray.length(); i++) {
                JSONObject transaction = transactionsArray.getJSONObject(i);

                TableRow row = new TableRow(this);

                TextView itemName = new TextView(this);
                itemName.setText(transaction.getString("itemName"));

                TextView quantity = new TextView(this);
                quantity.setText(transaction.getString("quantity"));

                TextView ratePerUnit = new TextView(this);
                ratePerUnit.setText(transaction.getString("ratePerUnit"));

                TextView amount = new TextView(this);
                amount.setText(transaction.getString("amount"));
                total += Integer.valueOf(transaction.getString("amount"));

                row.addView(itemName);
                row.addView(quantity);
                row.addView(ratePerUnit);
                row.addView(amount);

                // Add the TableRow to the TableLayout
                tableLayout.addView(row);
            }

            TextView amount = findViewById(R.id.textView5);
            TextView id = findViewById(R.id.textView2);
            TextView date = findViewById(R.id.textView3);
            String fullId = transactionItem.getString("uniqueId");
            int length = fullId.length();
            int startIndex = length / 2;
            String Id = fullId.substring(startIndex+1);
            id.setText("ID: " + Id);
            if (transactionItem.has("dateOfPurchase")) {
                date.setText(transactionItem.getString("dateOfPurchase"));
            } else if (transactionItem.has("dateOfSale")) {
                date.setText(transactionItem.getString("dateOfSale"));
            } else {
                // Handle the case when neither "dateOfPurchase" nor "dateOfSale" is available.
                date.setText("No date available");
            }

            amount.setText(String.valueOf(total));
            Log.d("Transaction Details", totalAmount);
            amount.setText(totalAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
