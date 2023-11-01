package com.example.appv1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.appv1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

public class RegisterIdProofsFragment extends Fragment
{
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;
    Spinner fpoDropdown;
    List<String> fpoOptions;
    String selectedFPOId;
    Button aadharCardButton;
    Button panCardButton;

    Uri selectedAadharCard;
    Uri selectedPanCard;
    TextInputEditText aadharNo, panNo;
    TextInputLayout aadharno, panno;
    TextView aadharT, panT, fpoT;
    JSONObject registerAsFarmerJson;

    public final static int PICK_PHOTO_CODE_AADHAR = 1046;
    public final static int PICK_PHOTO_CODE_PAN = 1047;

    public RegisterIdProofsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_id_proofs, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        aadharNo = getView().findViewById(R.id.register_id_aadhar_number);
        panNo = getView().findViewById(R.id.register_id_pan_card_number);

        aadharno = getView().findViewById(R.id.reg_aadhar_no);
        panno = getView().findViewById(R.id.reg_pan_no);

        aadharCardButton = getView().findViewById(R.id.register_id_card_button);
        addOnClickListenerForAadharCardButton();

        panCardButton = getView().findViewById(R.id.register_id_pan_card_button);
        addOnClickListenerForPanCardButton();

        aadharT = getView().findViewById(R.id.register_id_name);
        panT = getView().findViewById(R.id.register_id_pan_card);
        fpoT = getView().findViewById(R.id.register_id_fpo);

        registerAsFarmerJson = RegisterActivity.getRegisterAsFarmerJson();

        fpoDropdown = getView().findViewById(R.id.register_fpo_dropdown);
        fpoOptions = new ArrayList<>();

        // Fetch FPO options from the API
        fetchFPOOptions();

        // Set a listener for the dropdown selection
        fpoDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected FPO ID
                selectedFPOId = getSelectedFPOId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fpoT.setError("Select FPO");
            }
        });
    }

    public boolean checkFields() {
        String aadharNoS = aadharNo.getText().toString().trim();
        String panNoS = panNo.getText().toString().trim();
        String fpoS = selectedFPOId.toString().trim();

        if(selectedAadharCard == null) {
            aadharCardButton.setError("Upload aadhar");
            return false;
        }
        if(selectedPanCard == null) {
            panCardButton.setError("Upload PAN");
            return false;
        }

        if (aadharNoS.isEmpty()) {
            aadharno.setError("Enter aadhar number");
            return false;
        } else if(!aadharNoS.matches("^[0-9]+$")) {
            aadharno.setError("Only numbers allowed");
            return false;
        }else {
            aadharno.setError(null);
        }

        if (panNoS.isEmpty()) {
            panno.setError("Enter PAN number");
            return false;
        } else {
            panno.setError(null);
        }

        if (fpoS.isEmpty()) {
            fpoT.setError("Select FPO");
            return false;
        } else {
            fpoT.setError(null);
        }

        if(aadharT.getError() != null) {
            return false;
        }
        if(panT.getError() != null) {
            return false;
        }

        try {
            registerAsFarmerJson.put("panCardNumber", panNoS);
            registerAsFarmerJson.put("panCardImage", selectedPanCard.toString().trim());
            registerAsFarmerJson.put("aadharCardNumber", aadharNoS);
            registerAsFarmerJson.put("aadharCardImage", selectedAadharCard.toString().trim());
            registerAsFarmerJson.put("fpoId", fpoS);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addOnClickListenerForAadharCardButton() {
        aadharCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick(PICK_PHOTO_CODE_AADHAR);
            }
        });
    }

    private void addOnClickListenerForPanCardButton() {
        panCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick(PICK_PHOTO_CODE_PAN);
            }
        });
    }

    public void pick(int code) {
        String[] mimeTypes = {"image/png", "image/jpeg", "image/jpg", "image/jfif", "image/img"};

        // Create the gallery intent
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Create the camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            return; // Return early if permission is not granted
        }

        // Create an intent chooser that allows the user to select either camera or gallery
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose an image source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, code);
    }



    // Method to get the absolute path of the selected image from its URI
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_PHOTO_CODE_AADHAR) {
                selectedAadharCard = data.getData();
            } else if (requestCode == PICK_PHOTO_CODE_PAN) {
                selectedPanCard = data.getData();
            }
            if (selectedAadharCard != null) {
                String imageMimeType = getMimeType(selectedAadharCard);
                if (!isValidImageFormat(imageMimeType)) {
                    aadharT.setError("Invalid image format");
                }
            } else if (selectedPanCard != null) {
                String imageMimeType2 = getMimeType(selectedPanCard);
                if (!isValidImageFormat(imageMimeType2)) {
                    panT.setError("Invalid image format");
                }
            }
        }
    }

    private boolean isValidImageFormat(String mimeType) {
        return mimeType != null && (mimeType.equals("image/png") || mimeType.equals("image/jpeg") ||
                mimeType.equals("image/jpg") || mimeType.equals("image/jfif") || mimeType.equals("image/img"));
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri != null) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        }
        return mimeType;
    }


    private void fetchFPOOptions() {
        String url = getContext().getString(R.string.url) + "/list/fpo";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network request failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        // Parse the JSON response and extract the FPO options
                        JSONObject responseObject = new JSONObject(responseData);
                        JSONArray fpoArray = responseObject.getJSONArray("data");
                        Log.d("FPO Options", fpoArray.toString());
                        fpoOptions = extractFPOOptions(fpoArray);

                        // Update the UI on the main thread
                        Activity activity = getActivity();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Set the options in the Spinner adapter
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, fpoOptions);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                fpoDropdown.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private List<String> extractFPOOptions(JSONArray dataArray) throws JSONException {
        List<String> options = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject object = dataArray.getJSONObject(i);
            String fpoId = object.getString("_id");
            String userName = object.getString("userName");
            options.add(userName);
        }
        return options;
    }

    private String getSelectedFPOId(int position) {
        // Extract the FPO ID from the selected option
        String selectedOption = fpoOptions.get(position);
        String[] parts = selectedOption.split(" - ");
        if (parts.length > 0) {
            return parts[0];
        }
        return null;
    }
}
