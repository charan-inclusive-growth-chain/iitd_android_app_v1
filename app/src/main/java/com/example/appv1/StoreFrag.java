package com.example.appv1;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.AdapterView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.android.material.progressindicator.CircularProgressIndicator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFrag extends Fragment {
    GridView gridView;
    CircularProgressIndicator circularProgressIndicator;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFrag newInstance(String param1, String param2) {
        StoreFrag fragment = new StoreFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);
        circularProgressIndicator = rootView.findViewById(R.id.progressBar);
        gridView = rootView.findViewById(R.id.gridView);

        gridView.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);

        // Load and populate the data
        loadTrainingData();

        return rootView;
    }

    private void loadTrainingData() {
        String url = getContext().getString(R.string.url) + "/farmer/fpo/products";
        String token = LoginActivity.getToken(getContext()); // Use getContext() to get the context

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject responseObject = new JSONObject(responseData);
                        JSONArray dataArray = responseObject.getJSONArray("data");
                        Log.d("Store Data", dataArray.toString());

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateTrainingUpdates(dataArray);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void populateTrainingUpdates(JSONArray dataArray) {
        List<String> itemNames = new ArrayList<>();
        List<String> fpoPrices = new ArrayList<>();
        List<String> marketPrices = new ArrayList<>();
        List<String> images = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            try {
                JSONObject trainingObj = dataArray.getJSONObject(i);

                String available = trainingObj.getString("isAvailable");
                if (available.equals("false")) continue;

                String productName = trainingObj.getString("productName");
                String capitalizedProductName = productName.substring(0, 1).toUpperCase() + productName.substring(1);

                String marketPrice = trainingObj.getString("marketPrice");
                String fpoPrice = trainingObj.getString("fpoPrice");
                String imageUrl = trainingObj.getString("imageUrl");

                itemNames.add(capitalizedProductName);
                fpoPrices.add(fpoPrice);
                marketPrices.add(marketPrice);
                images.add(imageUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Create an adapter to populate the GridView
        StoreAdapter storeAdapter = new StoreAdapter(requireActivity(), itemNames, images, fpoPrices, marketPrices);
        circularProgressIndicator.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(storeAdapter);
    }
}