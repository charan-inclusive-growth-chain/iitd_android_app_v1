package com.example.appv1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoanAppForm3 extends Fragment
{
	EditText doorNumber, streetName, village, taluk, district;
	EditText state, pinCode, occupation, nop, residence, caste, religion;
	JSONObject loanApplicationJson;

	public LoanAppForm3()
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
		return inflater.inflate(R.layout.loan_app_frag3, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		doorNumber = view.findViewById(R.id.loan_door_number);
		streetName = view.findViewById(R.id.loan_street_name);
		village = view.findViewById(R.id.loan_village);
		taluk = view.findViewById(R.id.loan_taluk);
		district = view.findViewById(R.id.loan_district);
		state = view.findViewById(R.id.loan_state);
		pinCode = view.findViewById(R.id.loan_pin_code);
		occupation = view.findViewById(R.id.loan_occupation);
		nop = view.findViewById(R.id.loan_nop);
		residence = view.findViewById(R.id.loan_residence);
		caste = view.findViewById(R.id.loan_caste);
		religion = view.findViewById(R.id.loan_religion);
		loanApplicationJson = LoanApplication.getLoanApplicationJson();
		Log.d("Loan Form 3", loanApplicationJson.toString());
		loadDataFromFarmerProfileJson();
	}

//	private void loadData() {
//		String url = getContext().getString(R.string.url) + "/profile";
//		String token = LoginActivity.getToken(getContext());
//
//		OkHttpClient client = new OkHttpClient();
//		Request request = new Request.Builder()
//				.url(url)
//				.header("Authorization", "Bearer " + token)
//				.build();
//
//		client.newCall(request).enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				e.printStackTrace();
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				if (response.isSuccessful()) {
//					Handler handler = new Handler(Looper.getMainLooper());
//					handler.post(new Runnable() {
//						@Override
//						public void run() {
//							try {
//								String responseBody = response.body().string();
//								JSONObject jsonResponse = new JSONObject(responseBody);
//								doorNumber.setText(jsonResponse.getString("doorNumber"));
//								streetName.setText(jsonResponse.getString("streetName"));
//								village.setText(jsonResponse.getString("village"));
//								taluk.setText(jsonResponse.getString("taluk"));
//								district.setText(jsonResponse.getString("district"));
//								state.setText(jsonResponse.getString("state"));
//								pinCode.setText(jsonResponse.getString("pinCode"));
//								occupation.setText(jsonResponse.getString("occupation"));
//								nop.setText(jsonResponse.getString("natureOfPlace"));
//								residence.setText(jsonResponse.getString("residence"));
//								caste.setText(jsonResponse.getString("caste"));
//								religion.setText(jsonResponse.getString("religion"));
//							} catch (JSONException e) {
//								throw new RuntimeException(e);
//							} catch (IOException e) {
//								throw new RuntimeException(e);
//							}
//						}
//					});
//				}
//			}
//
//		});
//	}

	private void loadDataFromFarmerProfileJson() {
		// Check if the FarmerProfileJson object is available
		if (loanApplicationJson != null) {
			try {
				// Access the "profileData" from FarmerProfileJson
				String profileData = loanApplicationJson.optString("profileData", "");

				if (!profileData.isEmpty()) {
					JSONObject jsonResponse = new JSONObject(profileData);

					// Extract and set the details as before
					doorNumber.setText(jsonResponse.getString("doorNumber"));
					streetName.setText(jsonResponse.getString("streetName"));
					village.setText(jsonResponse.getString("village"));
					taluk.setText(jsonResponse.getString("taluk"));
					district.setText(jsonResponse.getString("district"));
					state.setText(jsonResponse.getString("state"));
					pinCode.setText(jsonResponse.getString("pinCode"));
					occupation.setText(jsonResponse.getString("occupation"));
					nop.setText(jsonResponse.getString("natureOfplace"));
					residence.setText(jsonResponse.getString("residence"));
					caste.setText(jsonResponse.getString("caste"));
					religion.setText(jsonResponse.getString("religion"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
