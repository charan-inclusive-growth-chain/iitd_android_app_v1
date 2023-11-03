package com.example.appv1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProfileFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView name, mobile, dob, gender, fatherName, motherName, address, occupation;
    TextView education, nop, residence, caste, religion, bankName, accountNumber;
    TextView ifsc, branchName, aadharCardNo, panCardNo;
    Button aadhar, pan;
    Button update, updatePass;
    String aadharUrl, panUrl;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2) {
        ProfileFrag fragment = new ProfileFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFrag() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ScrollView scrollView = view.findViewById(R.id.scrollviewbar);
        scrollView.setVerticalScrollBarEnabled(false);


        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        name = view.findViewById(R.id.profile_name);
        mobile = view.findViewById(R.id.profile_mobile);
        dob = view.findViewById(R.id.profile_dob);
        gender = view.findViewById(R.id.profile_gender);
        fatherName = view.findViewById(R.id.profile_father_name);
        motherName = view.findViewById(R.id.profile_mother_name);
        address = view.findViewById(R.id.profile_address);
        occupation = view.findViewById(R.id.profile_occupation);
        education = view.findViewById(R.id.profile_education);
        nop = view.findViewById(R.id.profile_nop);
        residence = view.findViewById(R.id.profile_residence);
        caste = view.findViewById(R.id.profile_caste);
        religion = view.findViewById(R.id.profile_religion);
        bankName = view.findViewById(R.id.profile_bank_name);
        accountNumber = view.findViewById(R.id.profile_account_number);
        ifsc = view.findViewById(R.id.profile_ifsc);
        branchName = view.findViewById(R.id.profile_branch_name);
        aadharCardNo = view.findViewById(R.id.profile_aadhar_card_number);
        aadhar = view.findViewById(R.id.profile_aadhar_image);
        panCardNo = view.findViewById(R.id.profile_pan_card_number);
        pan = view.findViewById(R.id.profile_pan_image);
        update = view.findViewById(R.id.edit_profile_btn);
        updatePass = view.findViewById(R.id.update_pass_btn);

        loadData();
        addOnClickListenerForAadhar();
        addOnclickListenerForPan();
        addOnClickListenerForUpdate();
        addOnClickListenerForUpdatePass();
    }

    private void loadData() {
        String url = getContext().getString(R.string.url) + "/profile";
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
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String responseBody = response.body().string();
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                name.setText(jsonResponse.getString("userName"));
                                mobile.setText(jsonResponse.getString("contactNumber"));
                                dob.setText(jsonResponse.getString("DOB"));
                                gender.setText(jsonResponse.getString("gender"));
                                fatherName.setText(jsonResponse.getString("fathersName"));
                                motherName.setText(jsonResponse.getString("mothersName"));

                                String doorNumber = jsonResponse.getString("doorNumber");
                                String street = jsonResponse.getString("streetName");
                                String village = jsonResponse.getString("village");
                                String taluk = jsonResponse.getString("taluk");
                                String district = jsonResponse.getString("district");
                                String state = jsonResponse.getString("state");
                                String pinCode = jsonResponse.getString("pinCode");

                                String addressString = doorNumber + ", " +
                                        street + ", " +
                                        village + ", " +
                                        taluk + ", " +
                                        district + ", " +
                                        state + " - " +
                                        pinCode;

                                address.setText(addressString);
                                occupation.setText(jsonResponse.getString("occupation"));
                                education.setText(jsonResponse.getString("education"));
                                nop.setText(jsonResponse.getString("natureOfplace"));
                                residence.setText(jsonResponse.getString("residence"));
                                caste.setText(jsonResponse.getString("caste"));
                                religion.setText(jsonResponse.getString("religion"));
                                bankName.setText(jsonResponse.getString("bankName"));
                                accountNumber.setText(jsonResponse.getString("accountNumber"));
                                ifsc.setText(jsonResponse.getString("ifscCode"));
                                branchName.setText(jsonResponse.getString("branchName"));
                                aadharCardNo.setText(jsonResponse.getString("aadharCardNumber"));
                                panCardNo.setText(jsonResponse.getString("panCardNumber"));
                                aadharUrl = jsonResponse.getString("aadharCardImage");
                                panUrl = jsonResponse.getString("panCardImage");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }

        });
    }

    private void addOnClickListenerForUpdate() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOnClickListenerForUpdatePass() {
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdatePassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOnClickListenerForAadhar() {
        aadhar.setOnClickListener(view -> {
            openImageAlert(aadharUrl); // Pass the Aadhar URL to the alert function
        });
    }

    private void addOnclickListenerForPan() {
        pan.setOnClickListener(view -> {
            openImageAlert(panUrl); // Pass the Pan URL to the alert function
        });
    }

    private void openImageAlert(String imageUrl) {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_image);
        ImageView imageView = dialog.findViewById(R.id.popupImageView);
        Log.d("ImageURL", imageUrl + " Received");

        // Load and display the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Image", "Loaded Succesfully");
                        // Image loaded successfully
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error (e.g., log the error)
                        e.printStackTrace();
                    }
                });


        dialog.show();
    }


}