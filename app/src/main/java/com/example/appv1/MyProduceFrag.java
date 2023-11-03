package com.example.appv1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import org.json.JSONArray;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyProduceFrag extends Fragment {

    GridView gridView;
    CircularProgressIndicator circularProgressIndicator;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MyProduceFrag() {
        // Required empty public constructor
    }

    public static MyProduceFrag newInstance(String param1, String param2) {
        MyProduceFrag fragment = new MyProduceFrag();
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
        View rootView = inflater.inflate(R.layout.fragment_my_produce, container, false);
        circularProgressIndicator = rootView.findViewById(R.id.progressBar);
        gridView = rootView.findViewById(R.id.gridView);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
        loadTrainingData();
        return rootView;
    }

    public void loadTrainingData() {
        String url = getContext().getString(R.string.url) + "/farmer/produce";
        String token = LoginActivity.getToken(getContext());
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
                        Log.d("Produce Data", dataArray.toString());


                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateProduceItems(dataArray);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void populateProduceItems(JSONArray dataArray) {
        List<String> lacStrainTypeList = new ArrayList<>();
        List<String> originList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        List<String> quantityList = new ArrayList<>();
        List<String> remarksList = new ArrayList<>();
        List<String> imageUrlList = new ArrayList();

        for (int i = 0; i < dataArray.length(); i++) {
            try {
                JSONObject produceObj = dataArray.getJSONObject(i);

                String lacStrainValue = produceObj.getString("lacStrainType");
                String originValue = produceObj.getString("origin");
                String sourceValue = produceObj.getString("treeSource");
                String quantityValue = produceObj.getString("quantity");
                String imageUrl = produceObj.getString("imageUrl");
                String remarksValue = produceObj.getString("remarks");

                lacStrainTypeList.add(lacStrainValue);
                originList.add(originValue);
                sourceList.add(sourceValue);
                imageUrlList.add(imageUrl);
                quantityList.add(quantityValue);
                remarksList.add(remarksValue);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MyProduceAdapter produceAdapter = new MyProduceAdapter(requireActivity(), lacStrainTypeList, originList, sourceList, quantityList, remarksList, imageUrlList);
        circularProgressIndicator.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(produceAdapter);
        // Now, you can use produceAdapter with your GridView to display the data.
    }
}
