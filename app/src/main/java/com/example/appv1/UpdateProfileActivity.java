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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateProfileActivity extends AppCompatActivity {
	final Calendar myCalendar = Calendar.getInstance();
	private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;
	Uri selectedAadharCard;
	Uri selectedPanCard;
	public final static int PICK_PHOTO_CODE_AADHAR = 1046;
	public final static int PICK_PHOTO_CODE_PAN = 1047;

	private String typeS, userName, mobile, dateOfBirth, age, gender;
	private String fatherName, motherName, doorNumber, street, village, taluk;
	private String district, state, pinCode, occupation, education, natureOfPlace;
	private String residence, caste, religion, bankName, accountNumber, ifscCode;
	private String branchName, panCardNumber, panCardImage, aadharCardNumber;
	private String aadharCardImage, fpoId;
	JSONObject jsonObject;
	EditText editUsername, editMobile, editGender, editDob, editFatherName, editMotherName, editDoorNo;
	EditText editStreet, editVillage, editTaluk, editDistrict, editState, editPincode;
	EditText editOccupation, editEducation, editNop, editResidence, editCaste, editReligion;
	EditText editBankName, editAccountNo, editIfsc, editBranchName, editPanNo, editAadharNo;
	Button uploadAadhar, uploadPan;

	Button save;
	String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_profile_layout);

		editUsername = findViewById(R.id.edit_username);
		editMobile = findViewById(R.id.edit_mobile);
		editDob = findViewById(R.id.edit_dob);
		editGender = findViewById(R.id.edit_gender);
		editFatherName = findViewById(R.id.edit_father_name);
		editMotherName = findViewById(R.id.edit_mother_name);
		editDoorNo = findViewById(R.id.edit_door_number);
		editStreet = findViewById(R.id.edit_street);
		editVillage = findViewById(R.id.edit_village);
		editTaluk = findViewById(R.id.edit_taluk);
		editDistrict = findViewById(R.id.edit_district);
		editState = findViewById(R.id.edit_state);
		editPincode = findViewById(R.id.edit_pin);
		editOccupation = findViewById(R.id.edit_occupation);
		editEducation = findViewById(R.id.edit_education);
		editNop = findViewById(R.id.edit_nop);
		editResidence = findViewById(R.id.edit_residence);
		editCaste = findViewById(R.id.edit_caste);
		editReligion = findViewById(R.id.edit_religion);
		editBankName = findViewById(R.id.edit_bank_name);
		editAccountNo = findViewById(R.id.edit_account_no);
		editIfsc = findViewById(R.id.edit_ifsc);
		editBranchName = findViewById(R.id.edit_branch);
		editPanNo = findViewById(R.id.edit_pan_no);
		editAadharNo = findViewById(R.id.edit_aadhar_no);
		uploadAadhar = findViewById(R.id.upload_aadhar);
		uploadPan = findViewById(R.id.upload_pan);

		save = findViewById(R.id.save_changes);
		addOnClickListenerForAadharCardButton();
		addOnClickListenerForPanCardButton();
		addOnClickListenerForSave();


		String url = getString(R.string.url) + "/userDetails";
		String token = LoginActivity.getToken(getApplicationContext());

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
					JSONObject jsonResponse = null;
					try {
						jsonResponse = new JSONObject(responseBody);
						userId = jsonResponse.getString("_id");
						response.close();
					} catch (JSONException e) {
						throw new RuntimeException(e);
					}
				}
			}

		});
	}

	private void addOnClickListenerForSave() {
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d("Click", "tk");

				JSONObject jsonObject = new JSONObject();
				try {
					if (editUsername != null) {
						userName = editUsername.getText().toString().trim();
						jsonObject.put("userName", userName);
					}
					if (editMobile != null) {
						mobile = editMobile.getText().toString().trim();
						jsonObject.put("contactNumber", mobile);
					}
					if (editDob != null) {
						dateOfBirth = editDob.getText().toString().trim();
						jsonObject.put("DOB", dateOfBirth);
					}
					if (editGender != null) {
						gender = editGender.getText().toString().trim();
						jsonObject.put("gender", gender);
					}
					if (editFatherName != null) {
						fatherName = editFatherName.getText().toString().trim();
						jsonObject.put("fathersName", fatherName);
					}
					if (editMotherName != null) {
						motherName = editMotherName.getText().toString().trim();
						jsonObject.put("mothersName", motherName);
					}
					if (editDoorNo != null) {
						doorNumber = editDoorNo.getText().toString().trim();
						jsonObject.put("doorNumber", doorNumber);
					}
					if (editStreet != null) {
						street = editStreet.getText().toString().trim();
						jsonObject.put("streetName", street);
					}
					if (editVillage != null) {
						village = editVillage.getText().toString().trim();
						jsonObject.put("village", village);
					}
					if (editTaluk != null) {
						taluk = editTaluk.getText().toString().trim();
						jsonObject.put("taluk", taluk);
					}
					if (editDistrict != null) {
						district = editDistrict.getText().toString().trim();
						jsonObject.put("district", district);
					}
					if (editState != null) {
						state = editState.getText().toString().trim();
						jsonObject.put("state", state);
					}
					if (editPincode != null) {
						pinCode = editPincode.getText().toString().trim();
						jsonObject.put("pinCode", pinCode);
					}
					if(editOccupation != null) {
						occupation = editOccupation.getText().toString().trim();
						jsonObject.put("occupation", occupation);
					}
					if (editEducation != null) {
						education = editEducation.getText().toString().trim();
						jsonObject.put("education", education);
					}
					if (editNop != null) {
						natureOfPlace = editNop.getText().toString().trim();
						jsonObject.put("natureOfplace", natureOfPlace);
					}
					if (editResidence != null) {
						residence = editResidence.getText().toString().trim();
						jsonObject.put("residence", residence);
					}
					if (editCaste != null) {
						caste = editCaste.getText().toString().trim();
						jsonObject.put("caste", caste);
					}
					if (editReligion != null) {
						religion = editReligion.getText().toString().trim();
						jsonObject.put("religion", religion);
					}
					if(editBankName != null) {
						bankName = editBankName.getText().toString().trim();
						jsonObject.put("bankName", bankName);
					}
					if (editAccountNo != null) {
						accountNumber = editAccountNo.getText().toString().trim();
						jsonObject.put("accountNumber", accountNumber);
					}
					if (editIfsc != null) {
						ifscCode = editIfsc.getText().toString().trim();
						jsonObject.put("ifscCode", ifscCode);
					}
					if (editBranchName != null) {
						branchName = editBranchName.getText().toString().trim();
						jsonObject.put("branchName", branchName);
					}
					if (panCardNumber != null) {
						panCardNumber = editPanNo.getText().toString().trim();
						jsonObject.put("panCardNumber", panCardNumber);
					}
					if (editAadharNo != null) {
						aadharCardNumber = editAadharNo.getText().toString().trim();
						jsonObject.put("aadharCardNumber", aadharCardNumber);
					}
					if (selectedAadharCard != null) {
						jsonObject.put("aadharCardImage", selectedAadharCard);
					}
					if (selectedPanCard != null) {
						jsonObject.put("panCardImage", selectedPanCard);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


				String url = getString(R.string.url) + "/profile/farmer";
				String token = LoginActivity.getToken(getApplicationContext());
				Log.d("JSON Object", jsonObject.toString());
				RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url(url)
						.header("Authorization", "Bearer " + token)
						.patch(requestBody)
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
							Log.d("Patch", responseBody);
							finish();
						}
					}
				});
			}
		});
	}

	private void addOnClickListenerForAadharCardButton() {
		uploadAadhar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pick(PICK_PHOTO_CODE_AADHAR);
			}
		});
	}

	private void addOnClickListenerForPanCardButton() {
		uploadPan.setOnClickListener(new View.OnClickListener() {
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
		if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			// Request camera permission
			ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
		}
	}

}

