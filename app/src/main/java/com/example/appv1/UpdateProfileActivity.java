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
	EditText  editMobile, editDoorNo;
	EditText editStreet, editVillage, editTaluk, editDistrict, editState, editPincode;
	EditText editOccupation, editEducation, editNop, editResidence;
	EditText editBankName, editAccountNo, editIfsc, editBranchName, editPanNo, editAadharNo;


	Button save;
	String userId;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_profile_layout);

		editMobile = findViewById(R.id.edit_mobile);

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

		editBankName = findViewById(R.id.edit_bank_name);
		editAccountNo = findViewById(R.id.edit_account_no);
		editIfsc = findViewById(R.id.edit_ifsc);
		editBranchName = findViewById(R.id.edit_branch);


		save = findViewById(R.id.save_changes);

		addOnClickListenerForSave();


		String url = getString(R.string.url) + "/profile/farmer";
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

					if (editMobile != null) {
						mobile = editMobile.getText().toString().trim();
						if(!mobile.equals("")){
							jsonObject.put("contactNumber", mobile);
						}
					}

					if (editDoorNo != null) {
						doorNumber = editDoorNo.getText().toString().trim();
						if(!doorNumber.equals("")) {
							jsonObject.put("doorNumber", doorNumber);
						}
					}
					if (editStreet != null) {
						street = editStreet.getText().toString().trim();
						if(!street.equals("")) {
							jsonObject.put("streetName", street);
						}
					}
					if (editVillage != null) {
						village = editVillage.getText().toString().trim();
						if(!village.equals("")){
							jsonObject.put("village", village);
						}
					}
					if (editTaluk != null) {
						taluk = editTaluk.getText().toString().trim();
						if(!taluk.equals("")) {
							jsonObject.put("taluk", taluk);
						}
					}
					if (editDistrict != null) {
						district = editDistrict.getText().toString().trim();
						if(!district.equals("")) {
							jsonObject.put("district", district);
						}
					}
					if (editState != null) {
						state = editState.getText().toString().trim();
						if(!state.equals("")) {
							jsonObject.put("state", state);
						}
					}
					if (editPincode != null) {
						pinCode = editPincode.getText().toString().trim();
						if(!pinCode.equals("")) {
							jsonObject.put("pinCode", pinCode);
						}
					}
					if(editOccupation != null) {
						occupation = editOccupation.getText().toString().trim();
						if(!occupation.equals("")) {
							jsonObject.put("occupation", occupation);
						}
					}
					if (editEducation != null) {
						education = editEducation.getText().toString().trim();
						if(!education.equals("")) {
							jsonObject.put("education", education);
						}
					}
					if (editNop != null) {
						natureOfPlace = editNop.getText().toString().trim();
						if(natureOfPlace.equals("")) {
							jsonObject.put("natureOfplace", natureOfPlace);
						}
					}
					if (editResidence != null) {
						residence = editResidence.getText().toString().trim();
						if(!residence.equals("")){
							jsonObject.put("residence", residence);
						}
					}

					if(editBankName != null) {
						bankName = editBankName.getText().toString().trim();
						if(!bankName.equals("")){
							jsonObject.put("bankName", bankName);
						}
					}
					if (editAccountNo != null) {
						accountNumber = editAccountNo.getText().toString().trim();
						if(!accountNumber.equals("")){
							jsonObject.put("accountNumber", accountNumber);
						}
					}
					if (editIfsc != null) {
						ifscCode = editIfsc.getText().toString().trim();
						if(!ifscCode.equals("")){
							jsonObject.put("ifscCode", ifscCode);
						}
					}
					if (editBranchName != null) {
						branchName = editBranchName.getText().toString().trim();
						if(!branchName.equals("")){
							jsonObject.put("branchName", branchName);
						}
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
							Intent nextIntent = new Intent(UpdateProfileActivity.this, MainActivity.class);
							UpdateProfileActivity.this.startActivity(nextIntent);
						}
					}
				});
			}
		});
	}









	// Method to get the absolute path of the selected image from its URI


}

