package com.example.appv1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceRequirementFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceRequirementFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] inputs = {"Brood Lac", "Nylon Bag", "Insecticide"};



    public PlaceRequirementFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceRequirementFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceRequirementFrag newInstance(String param1, String param2) {
        PlaceRequirementFrag fragment = new PlaceRequirementFrag();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_requirement, container, false);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_month, months);
        ArrayAdapter<String> inputAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_month, inputs);
        AutoCompleteTextView autoCompleteTextViewMonth = view.findViewById(R.id.month_dropdown);
        AutoCompleteTextView autoCompleteTextViewInput = view.findViewById(R.id.input_dropdown);
        autoCompleteTextViewMonth.setAdapter(monthAdapter);
        autoCompleteTextViewInput.setAdapter(inputAdapter);

        Button submitButton = view.findViewById(R.id.submit);
        EditText originField = view.findViewById(R.id.origin);
        EditText quantityField = view.findViewById(R.id.quantity);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment voiceFragment = new VoiceBotFrag();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, voiceFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMonth = autoCompleteTextViewMonth.getText().toString();
                String inputType = autoCompleteTextViewInput.getText().toString();
                String brandName =  originField.getText().toString();
                String quantity = quantityField.getText().toString();

                // Build the JSON request body
                MediaType mediaType = MediaType.parse("application/json");
                String requestBodyJson = String.format(
                        "{\n" +
                                "    \"type\":\"%s\",\n" +
                                "    \"quantity\":%s,\n" +
                                "    \"month\":\"%s\",\n" +
                                "    \"Brand\":\"%s\"\n" +
                                "}",
                        inputType, quantity,selectedMonth, brandName
                );
                Log.d("request Body", requestBodyJson.toString());

                // Create an OkHttpClient instance
                OkHttpClient client = new OkHttpClient();

                // Create the request with the JSON body
                RequestBody body = RequestBody.create(mediaType, requestBodyJson);
                Request request = new Request.Builder()
                        .url(getString(R.string.url) + "/farmer/requirements")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + LoginActivity.getToken(getContext()))
                        .build();

                // Send the request and handle the response
//                try {
//                    Response response = client.newCall(request).execute();
//                    // Handle the response as needed
//                    Log.d("Form Submitted", response.toString());
//                } catch (Exception e) {
//                    Log.d("error", "Coudlnt Request");
//                    e.printStackTrace();
//                    // Handle any exceptions that may occur during the request
//                }

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful()) {
                            Log.d("Form Submitted", response.toString());

                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "Requirement Submitted Successfully", Toast.LENGTH_LONG).show();
                                }
                            });

                            DashboardFrag dashboardFrag = new DashboardFrag();
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout, dashboardFrag);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                        else {
                            Log.d("Failed", "Request Failed");
                        }

                    }
                });


            }
        });


        return view;
    }
}