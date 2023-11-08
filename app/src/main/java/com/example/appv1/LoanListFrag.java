package com.example.appv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoanListFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoanListFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static String WindowID;
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button apply_loan;

    public LoanListFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoanListFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static LoanListFrag newInstance(String param1, String param2) {
        LoanListFrag fragment = new LoanListFrag();
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
        View view = inflater.inflate(R.layout.fragment_loan_list, container, false);
        apply_loan = view.findViewById(R.id.apply_loan);
        addOnClickListenerForApplyLoan();

        // Initialize your ViewPager2 and TabLayout here
        ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Create an adapter for the ViewPager2
        VPLoanAdapter vpLoanAdapter = new VPLoanAdapter(this);
        vpLoanAdapter.addFragment(new LoanApprovedTabFrag(), "Approved");
        vpLoanAdapter.addFragment(new LoanPendingTabFrag(), "Pending");
        vpLoanAdapter.addFragment(new LoanRejectedTabFrag(), "Rejected");

        viewPager.setAdapter(vpLoanAdapter);

        // Attach the TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(vpLoanAdapter.getFragmentTitle(position));
        }).attach();



        return view;
    }

    private void addOnClickListenerForApplyLoan()
    {
        apply_loan.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {

                if(apply_loan.isEnabled() == true) {
                    callLoanApplication();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Alert")
                            .setMessage("Loan window is not active for this account.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void checkIfCanApplyForLoan() {
        String url = getContext().getString(R.string.url) + "/api/activeLoanWindow/farmer";
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
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    JSONArray jsonArray = jsonResponse.getJSONArray("data");
                                    if (jsonArray.length() <= 0) {
                                        apply_loan.setEnabled(false);
                                    } else {
                                        JSONObject object = jsonArray.getJSONObject(0);
                                        WindowID = object.getString("loanWindowId");
                                        apply_loan.setEnabled(true);
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void callLoanApplication()
    {
        Intent loanAppIntent = new Intent(getActivity(), LoanApplication.class);
        getActivity().startActivity(loanAppIntent);
    }
}