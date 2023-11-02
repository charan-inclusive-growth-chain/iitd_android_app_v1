package com.example.appv1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoanAppForm2 extends Fragment
{
	EditText dobT, ageT, aadharNumberT;
	EditText panNumberT, fatherNameT, motherNameT;
	Button aadhar, pan;
	String age, aadharUrl, panUrl;

	public LoanAppForm2()
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
		return inflater.inflate(R.layout.loan_app_frag2, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		dobT = view.findViewById(R.id.loan_dob);
		aadharNumberT = view.findViewById(R.id.loan_aadhar_number);
		panNumberT = view.findViewById(R.id.loan_pan_card_number);
		fatherNameT = view.findViewById(R.id.loan_father_name);
		motherNameT = view.findViewById(R.id.loan_mother_name);
		aadhar = view.findViewById(R.id.loan_aadhar);
		pan = view.findViewById(R.id.loan_pan);
		loadData();
		addOnClickListenerForAadhar();
		addOnclickListenerForPan();
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
								dobT.setText(jsonResponse.getString("dateOfBirth"));
								setAge(String.valueOf(dobT.getText()));
								fatherNameT.setText(jsonResponse.getString("fatherName"));
								motherNameT.setText(jsonResponse.getString("motherName"));
								aadharNumberT.setText(jsonResponse.getString("aadharCardNumber"));
								panNumberT.setText(jsonResponse.getString("panCardNumber"));
								aadharUrl = "http://3.7.253.48:3000/img/" + jsonResponse.getString("aadharCardImage");
								panUrl = "http://3.7.253.48:3000/img/" + jsonResponse.getString("panCardImage");
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

	private String setAge(String dob) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

		try {
			Date birthDate = dateFormat.parse(dob);
			Calendar today = Calendar.getInstance();
			Calendar birthCalendar = Calendar.getInstance();
			birthCalendar.setTime(birthDate);

			int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

			if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
				age--;
			}

			return String.valueOf(age);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
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
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		builder.setTitle("View Image");

		View view = getLayoutInflater().inflate(R.layout.image_alert_layout, null);

		ImageView imageView = view.findViewById(R.id.alert_image_view);
		imageView.setImageDrawable(null);

		new DisplayImage(imageView).execute(imageUrl);

		builder.setView(view);
		builder.setPositiveButton("OK", (dialog, which) -> {
			dialog.dismiss();
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}


}
