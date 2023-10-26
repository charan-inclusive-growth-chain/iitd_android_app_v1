package com.example.appv1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appv1.databinding.FragmentEnterProduceTabBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterProduceTabFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterProduceTabFrag extends Fragment {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;
    public final static int PICK_PHOTO_CODE = 1048;
    @NonNull FragmentEnterProduceTabBinding binding;

    MaterialButton uploadImgBtn, submit;
    Uri selectedImage;
    TextInputEditText type, origin, source, quantity, remarks;
    TextInputLayout t, or, s, q, r;
    TextView uploadImgText;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnterProduceTabFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterProduceTabFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterProduceTabFrag newInstance(String param1, String param2) {
        EnterProduceTabFrag fragment = new EnterProduceTabFrag();
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
        binding = FragmentEnterProduceTabBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        uploadImgBtn = binding.uploadImageBtn;
        submit = binding.submit;
        type = binding.lacType;
        origin = binding.origin;
        source = binding.sourceOfTree;
        quantity = binding.quantity;
        remarks = binding.remarks;
        t = binding.lacTypeL;
        or = binding.originL;
        s = binding.sourceL;
        q = binding.quantityL;
        r = binding.remarksL;
        uploadImgText = binding.uploadImageText;


        addOnClickListenerForUploadButton();
        addOnclickListenerForSubmit();
    }

    private void addOnClickListenerForUploadButton()
    {
        uploadImgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                pick(PICK_PHOTO_CODE);
            }
        });
    }

    public void pick(int code) {
        String[] mimeTypes = {"image/png", "image/jpeg", "image/jpg", "image/jfif", "image/img"};

        if (code == PICK_PHOTO_CODE) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            Intent chooserIntent = Intent.createChooser(galleryIntent, "Open Gallery");
            startActivityForResult(chooserIntent, code);
        } else if (code == CAMERA_PERMISSION_REQUEST_CODE) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) requireContext(),
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                startActivityForResult(cameraIntent, code);
            }
        }
    }



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
                        uploadImgText.setError("Invalid image format");
                    }
                }
            }
        }
    }


    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri != null) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        }
        return mimeType;
    }

    private void addOnclickListenerForSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lacStrainType = type.getText().toString();
                String sourceOfTreeT = source.getText().toString();
                String originText = origin.getText().toString();
                String quantityText = quantity.getText().toString();
                String remarksText = remarks.getText().toString();

                if (lacStrainType.isEmpty() || sourceOfTreeT.isEmpty() || originText.isEmpty()
                        || quantityText.isEmpty() || remarksText.isEmpty() || selectedImage == null) {
                    if (lacStrainType.isEmpty()) {
                        t.setError("Lac Strain Type cannot be empty");
                    }
                    if (sourceOfTreeT.isEmpty()) {
                        s.setError("Source of Tree cannot be empty");
                    }
                    if (originText.isEmpty()) {
                        or.setError("Origin cannot be empty");
                    }
                    if (quantityText.isEmpty()) {
                        q.setError("Quantity cannot be empty");
                    }
                    if (remarksText.isEmpty()) {
                        r.setError("Remarks cannot be empty");
                    }
                    if(selectedImage == null) {
                        uploadImgText.setError("Upload image failed");
                    }
                } else {
                    MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("productImg", "filename.png", RequestBody.create(MediaType.parse("image/png"), String.valueOf(selectedImage)))
                            .addFormDataPart("farmerId", LoginActivity.getFarmerID(getContext()))
                            .addFormDataPart("lacStrainType", lacStrainType)
                            .addFormDataPart("treeSource", sourceOfTreeT)
                            .addFormDataPart("origin", originText)
                            .addFormDataPart("quantity", quantityText)
                            .addFormDataPart("remarks", remarksText);

                    RequestBody requestBody = requestBodyBuilder.build();
                    Log.d("FormData", requestBody.toString());

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(getString(R.string.url) + "/farmer/produce")
                            .addHeader("Authorization", "Bearer " + LoginActivity.getToken(getContext()))
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Log.d("Form submitted", response.toString());
                                SellToFPOTabFrag sellToFPOTabFragment = new SellToFPOTabFrag();
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout, sellToFPOTabFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else {
                            }
                        }
                    });
                }
            }
        });
    }
}