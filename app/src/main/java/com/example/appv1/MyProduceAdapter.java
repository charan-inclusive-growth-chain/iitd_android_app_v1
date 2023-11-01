package com.example.appv1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.app.Dialog;

import java.util.List;
public class MyProduceAdapter extends BaseAdapter {

    private Context context;
    private List<String> lacStrainTypeList;
    private List<String> originList;
    private List<String> sourceList;
    private List<String> quantityList;
    private List<String> remarksList;
    private List<String> imageUrlList;

    LayoutInflater inflater;

    public MyProduceAdapter(Context context, List<String> lacStrainTypeList, List<String> originList, List<String> sourceList, List<String> quantityList, List<String> remarksList, List<String> imageUrlList) {
        this.context = context;
        this.lacStrainTypeList = lacStrainTypeList;
        this.originList = originList;
        this.sourceList = sourceList;
        this.quantityList = quantityList;
        this.remarksList = remarksList;
        this.imageUrlList = imageUrlList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.produce_item, null);

        }
        // Initialize your views
        ImageView imageView = convertView.findViewById(R.id.grid_image);
        ImageButton infoButton = convertView.findViewById(R.id.infoProduce);

        String imageUrl = imageUrlList.get(position);

        new DisplayImage(imageView).execute(imageUrl);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(position);
            }
        });

        return convertView;
    }

    private void showInfoDialog(int position) {
        // Create and configure a dialog
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.produce_dialog);

        // Initialize the views in the dialog
        TextView lacStrainValue = dialog.findViewById(R.id.lacStrainValue);
        TextView originValue = dialog.findViewById(R.id.originValue);
        TextView sourceValue = dialog.findViewById(R.id.sourceValue);
        TextView quantityValue = dialog.findViewById(R.id.quantityValue);
        TextView remarksValue = dialog.findViewById(R.id.remarksValue);

        // Set values from the lists based on the selected position
        lacStrainValue.setText(lacStrainTypeList.get(position));
        originValue.setText(originList.get(position));
        sourceValue.setText(sourceList.get(position));
        quantityValue.setText(quantityList.get(position));
        remarksValue.setText(remarksList.get(position));

        // Show the dialog
        dialog.show();
    }
}
