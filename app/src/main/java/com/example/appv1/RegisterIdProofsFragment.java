package com.example.appv1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.ContentResolver;

public class RegisterIdProofsFragment extends Fragment
{
    // Constants for permissions and request codes
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int PICK_PHOTO_FROM_GALLERY = 102;
    private static final int CAPTURE_PHOTO = 103;

    public final static int PICK_PHOTO_CODE = 1048;

    Spinner fpoDropdown;
    List<String> fpoOptions;
    String selectedFPOId;
    Button aadharCardButton;
    Button panCardButton;

    Uri selectedAadharCard;
    Uri selectedPanCard;
    Uri selectedImage;
    TextInputEditText aadharNo, panNo;
    TextInputLayout aadharno, panno;
    TextView aadharT, panT, fpoT;
    JSONObject registerAsFarmerJson;
    List<String> FpoIdList = new ArrayList<>();

    String aadharCardUrl = "";

    String panCardUrl = "";
    private int currentRequestCode;

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

        if(aadharCardUrl.equals("")) {
            aadharCardButton.setError("Upload aadhar");
            return false;
        }
        if(panCardUrl.equals("")) {
            panCardButton.setError("Upload PAN");
            return false;
        }

        if (aadharNoS.isEmpty()) {
            aadharno.setError("Enter Aadhar number");
            return false;
        } else if (!aadharNoS.matches("^[0-9]+$")) {
            aadharno.setError("Only numbers allowed");
            return false;
        } else if (aadharNoS.length() != 12) {
            aadharno.setError("Aadhar number should be 12 digits long");
            return false;
        } else {
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
            registerAsFarmerJson.put("panCardImage", panCardUrl);
            registerAsFarmerJson.put("aadharCardNumber", aadharNoS);
            registerAsFarmerJson.put("aadharCardImage", aadharCardUrl);

            registerAsFarmerJson.put("fpoId", fpoS);

            Log.d("IDProofDetails", registerAsFarmerJson.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addOnClickListenerForAadharCardButton() {
        aadharCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    currentRequestCode = PICK_PHOTO_CODE_AADHAR; // Set the current request code
                    pick(PICK_PHOTO_CODE);
                }
            }
        });
    }

    private void addOnClickListenerForPanCardButton() {
        panCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for camera permission
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    currentRequestCode = PICK_PHOTO_CODE_PAN; // Set the current request code
                    pick(PICK_PHOTO_CODE);
                }
            }
        });
    }

    // Open image picker (either gallery or camera)
    private void openImagePicker(int requestCode) {
        String[] mimeTypes = {"image/png", "image/jpeg", "image/jpg", "image/jfif", "image/img"};

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose an image source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, requestCode);
    }

    public void pick(int code) {
        String[] mimeTypes = {"image/png", "image/jpeg", "image/jpg", "image/jfif", "image/img"};

        if (code == PICK_PHOTO_CODE) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            Intent chooserIntent = Intent.createChooser(galleryIntent, "Open Gallery");
            startActivityForResult(chooserIntent, code);
        } else if (code == REQUEST_CAMERA_PERMISSION) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) requireContext(),
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);
            } else {
                startActivityForResult(cameraIntent, code);
            }
        }
    }





    // Method to get the absolute path of the selected image from its URI
    // Method to get the absolute path of the selected image from its URI
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_PHOTO_CODE) {
                selectedImage = data.getData();
                String imageMimeType = getMimeType(selectedImage);
                if (imageMimeType != null) {
                    if (!imageMimeType.equals("image/png") &&
                            !imageMimeType.equals("image/jpeg") &&
                            !imageMimeType.equals("image/jpg") &&
                            !imageMimeType.equals("image/jfif") &&
                            !imageMimeType.equals("image/img")) {
                        //uploadImgText.setError("Invalid image format");
                    }
                    else {
                        uploadImageToServer(selectedImage);
                    }
                }
            }
        }
    }

    // Method to upload the image to the Node.js API

    private void uploadImageToServer(Uri imageUri) {
        try {


            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("doc", "file.jpg", RequestBody.create(MediaType.parse(getMimeType(selectedImage)), getImageFileFromUri(selectedImage)));
            RequestBody requestBody = requestBodyBuilder.build();
            Log.d("FormData Image", requestBody.toString());


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(getContext().getString(R.string.url) + "/document")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Failure", "Failed image Upload following error:");
                    e.printStackTrace();
                    // Handle failure, e.g., show an error message to the user
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        try {
                            JSONObject responseObject = new JSONObject(responseData);
                            final String imageUrl = responseObject.getJSONObject("data").getString("docId");

                            Log.d("Image Upload", imageUrl);

                            // Set the URL based on the currentRequestCode
                            if (currentRequestCode == PICK_PHOTO_CODE_AADHAR) {
                                aadharCardUrl = imageUrl;
                            } else if (currentRequestCode == PICK_PHOTO_CODE_PAN) {
                                panCardUrl = imageUrl;
                            }
                            // Now you have the image URL, you can use it as needed
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update UI or store the image URL in a variable
                                    // imageUrl is the URL of the uploaded image on AWS

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility function to get the file path from a Uri
    private File getImageFileFromUri(Uri uri) {
        String fileName = "image.png"; // Set your desired file name and extension
        File imageFile = new File(requireContext().getCacheDir(), fileName);

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                OutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
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
            FpoIdList.add(fpoId);
            options.add(userName);
        }
        return options;
    }

    private String getSelectedFPOId(int position) {
        // Extract the FPO ID from the selected option
//        String selectedOption = fpoOptions.get(position);
//        String[] parts = selectedOption.split(" - ");
//        if (parts.length > 0) {
//            return parts[0];
//        }
        Log.d("FPOID", FpoIdList.get(position));

        return FpoIdList.get(position);
    }
}
