package com.example.appv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StoreAdapter extends BaseAdapter {

    private Context context;
    private List<String> itemNames;
    private List<String> images;
    private List<String> fpoPrices;
    private List<String> marketPrices;

    private LayoutInflater inflater;

    public StoreAdapter(Context context, List<String> itemNames, List<String> images, List<String> fpoPrices, List<String> marketPrices) {
        this.context = context;
        this.itemNames = itemNames;
        this.images = images;
        this.fpoPrices = fpoPrices;
        this.marketPrices = marketPrices;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemNames.size();
    }

    @Override
    public Object getItem(int position) {
        return itemNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.store_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image);
        TextView itemTextView = convertView.findViewById(R.id.item_name);
        TextView fpoPriceTextView = convertView.findViewById(R.id.store_fpo);
        TextView marketPriceTextView = convertView.findViewById(R.id.store_market);
        new DisplayImage(imageView).execute(images.get(position));
        itemTextView.setText(itemNames.get(position));
//        fpoPriceTextView.setText("FPO Price " + "     : ₹" + fpoPrices.get(position));
//        marketPriceTextView.setText("Market Price " + ": ₹" + marketPrices.get(position));
        String fpoLabel = context.getString(R.string.fpo);
        String marketLabel = context.getString(R.string.market);
        String rupeeSymbol = context.getString(R.string.rupee_symbol);
        // Set text using string resources
        fpoPriceTextView.setText(String.format("%-14s :  %s%s", fpoLabel, rupeeSymbol, fpoPrices.get(position)));
        marketPriceTextView.setText(String.format("%s : %s%s", marketLabel, rupeeSymbol, marketPrices.get(position)));

        return convertView;
    }
}
