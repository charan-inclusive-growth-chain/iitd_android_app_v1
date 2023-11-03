package com.example.appv1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
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
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaleHistoryTabFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaleHistoryTabFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    CircularProgressIndicator circularProgressIndicator;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TransactionHistoryAdapter adapter;

    public SaleHistoryTabFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaleHistoryTabFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SaleHistoryTabFrag newInstance(String param1, String param2) {
        SaleHistoryTabFrag fragment = new SaleHistoryTabFrag();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_history_tab, container, false);
        circularProgressIndicator = view.findViewById(R.id.progressBar);
        ListView listView = view.findViewById(R.id.listview);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        loadData();
        return view;
    }

    private void loadData() {
        String url = getContext().getString(R.string.url) + "/fpo/transaction/api/transactions/" + LoginActivity.getFarmerID(getContext()) + "/sale";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + LoginActivity.getToken(getContext()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseBody);
                        Log.d("Sales History Data", jsonArray.toString());
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    List<JSONObject> salesHistory = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        salesHistory.add(object);
                                    }
                                    adapter = new TransactionHistoryAdapter(getContext(), salesHistory);
                                    ListView listView = getView().findViewById(R.id.listview);
                                    circularProgressIndicator.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    listView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}