package com.example.appv1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoanAppForm1 extends Fragment
{
	JSONObject loanObject;
	String fpo;
	Spinner fpoDropdown;
	List<String> fpoOptions;
	String selectedFPOId;
	EditText fpoT, bankT, accountNumberT, ifscT;
	EditText branchNameT, nameT, genderT, mobileT;

	public LoanAppForm1()
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
		return inflater.inflate(R.layout.loan_app_frag1, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		bankT = getView().findViewById(R.id.loan_bank_name);
		mobileT = getView().findViewById(R.id.loan_account_number);
		ifscT = getView().findViewById(R.id.loan_ifsc);
		branchNameT = getView().findViewById(R.id.loan_branch);
		accountNumberT = getView().findViewById(R.id.loan_account_number);
		mobileT = getView().findViewById(R.id.loan_mobile);
		fpoT = getView().findViewById(R.id.loan_fpo);
		nameT = getView().findViewById(R.id.loan_name);
		genderT = getView().findViewById(R.id.loan_gender);
		loadData();
		fetchFPOOptions();
	}

	private void loadData() {
		String url = getContext().getString(R.string.url) + "/userDetails";
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
								nameT.setText(jsonResponse.getString("userName"));
								mobileT.setText(jsonResponse.getString("mobile"));
								genderT.setText(jsonResponse.getString("gender"));
								bankT.setText(jsonResponse.getString("bankName"));
								accountNumberT.setText(jsonResponse.getString("accountNumber"));
								ifscT.setText(jsonResponse.getString("ifscCode"));
								branchNameT.setText(jsonResponse.getString("branchName"));
								fpo = jsonResponse.getString("fpoId");

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

	private void fetchFPOOptions() {
		String url = getContext().getString(R.string.url) + "/signup/fpo";

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
						JSONObject responseObject = new JSONObject(responseData);
						JSONArray fpoArray = responseObject.getJSONArray("data");
						for (int i = 0; i < fpoArray.length(); i++) {
							JSONObject object = fpoArray.getJSONObject(i);
							String fpoId = object.getString("_id");
							String userName = object.getString("userName");
							if(fpoId.equals(fpo)) {
								fpoT.setText(userName);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
