package com.example.appv1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoanAppForm4 extends Fragment
{
	static Context context;
	final Calendar myCalendar = Calendar.getInstance();
	ArrayList<String> loanWindowIDs;
	static EditText dateOfBirth;
	static EditText applicantName;
	static EditText age;
	static EditText relationship;
	static EditText landHolding;
	static EditText typeOfLandHolding;
	static EditText monthlyHHIncome;
	static EditText monthlyHHExpenses;
	static EditText requestedAmount;
	EditText purpose;
	EditText tenure;
	EditText interest;
	EditText loanID;
	static TextView dobT;
	static TextView nameT;
	static TextView ageT;
	static TextView relationshipT;
	static TextView landHoldingT;
	static TextView typeoflandHoldingT;
	static TextView incomeT;
	static TextView expensesT;
	static TextView amountT;
	static TextView purposeT;
	static TextView tenureT;
	static String interestS;

	static String fpoNameS;

	static String idS;
	static String fpoIdS;

	static String loanIdS;

	static String windowIdS;
	RadioButton male, female;
	RadioGroup genderRadioGroup;
	static String gender;
	static JSONObject loanApplicationJson;



	public LoanAppForm4()
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
		return inflater.inflate(R.layout.loan_app_frag4, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		context = getContext();
		dateOfBirth = getView().findViewById(R.id.loan_co_dob);
		applicantName = getView().findViewById(R.id.loan_co_name);
		genderRadioGroup = getView().findViewById(R.id.loan_co_gender);
		male = getView().findViewById(R.id.loan_male);
		female = getView().findViewById(R.id.loan_female);
		age = getView().findViewById(R.id.loan_co_age);
		relationship = getView().findViewById(R.id.loan_app_applicant_relationship);
		landHolding = getView().findViewById(R.id.loan_app_land_holding);
		typeOfLandHolding = getView().findViewById(R.id.loan_app_land_holding_type);
		monthlyHHIncome = getView().findViewById(R.id.loan_app_monthly_income);
		monthlyHHExpenses = getView().findViewById(R.id.loan_app_monthly_expense);
		requestedAmount = getView().findViewById(R.id.loan_app_requested_amount);
		purpose = getView().findViewById(R.id.loan_app_loan_purpose);
		tenure = getView().findViewById(R.id.loan_app_tenure_month);
		interest = getView().findViewById(R.id.loan_app_interest_rate);
		loanID = getView().findViewById(R.id.loan_app_loan_id);

		dobT = getView().findViewById(R.id.loan_co_dob_text);
		nameT = getView().findViewById(R.id.loan_name_text);
		ageT = getView().findViewById(R.id.loan_co_age_text);
		relationshipT = getView().findViewById(R.id.loan_relationship_text);
		landHoldingT = getView().findViewById(R.id.loan_land_holding_text);
		typeoflandHoldingT = getView().findViewById(R.id.loan_land_type_text);
		incomeT = getView().findViewById(R.id.loan_income_text);
		expensesT = getView().findViewById(R.id.loan_expense_text);
		amountT = getView().findViewById(R.id.loan_amount_text);
		purposeT = getView().findViewById(R.id.loan_app_loan_purpose);
		tenureT = getView().findViewById(R.id.loan_app_tenure_month);


		addOnClickListenerForDOBButton();
		genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				if (checkedId == R.id.radio_pirates) {
					gender = "Male";
				} else if (checkedId == R.id.radio_ninjas) {
					gender = "Female";
				}
			}
		});

		createUniqueID();
		getInterestRate();
		//submit();
	}

	public static void submit(Button submitBtn) {
		loanApplicationJson = LoanApplication.getLoanApplicationJson();
		JSONObject requestData = new JSONObject();
		Log.d("Submit BUtton Clicked", "Submisdsion started");
		// Retrieve values from EditText fields
		String nameS = applicantName.getText().toString().trim();
		String dobS = dateOfBirth.getText().toString().trim();
		String ageS = age.getText().toString().trim();
		String relationshipS = relationship.getText().toString().trim();
		String landHoldingS = landHolding.getText().toString().trim();
		String landTypeS = typeOfLandHolding.getText().toString().trim();
		String incomeS = monthlyHHIncome.getText().toString().trim();
		String expensesS = monthlyHHExpenses.getText().toString().trim();
		String amountS = requestedAmount.getText().toString().trim();
		String purposeS = purposeT.getText().toString().trim();
		String tenureS = tenureT.getText().toString().trim();

		Log.d("Values Tenure", tenureS);
		Log.d("Values purpsoe", purposeS);
		Log.d("Values amountS", amountS);

		boolean empty = false;

		if(nameS.isEmpty()) {
			nameT.setError("Enter co-applicant name");
			empty = true;

		}
		if(dobS.isEmpty()) {
			dobT.setError("Enter age");
			empty = true;
		}

		if(ageS.isEmpty()) {
			ageT.setError("Enter age");
			empty = true;
		}

		if(relationshipS.isEmpty()) {
			relationshipT.setError("Enter relationship");
			empty = true;
		}

		if(landHoldingS.isEmpty()) {
			landHoldingT.setError("Enter land holding");
			empty = true;
		}
		if(landTypeS.isEmpty()) {
			typeoflandHoldingT.setError("Enter land holding type");
			empty = true;
		}
		if(incomeS.isEmpty()) {
			incomeT.setError("Enter monthly hh income");
			empty = true;
		}

		if(expensesS.isEmpty()) {
			expensesT.setError("Enter monthly hh expenses");
			empty = true;
		}

		if(amountS.isEmpty()) {
			amountT.setError("Enter requested amount");
			empty = true;
		}
		if(purposeS.isEmpty()) {
			purposeT.setError("Purpose is Requried");
			empty = true;
		}
		if(tenureS.isEmpty()){
			tenureT.setError("Loan Tenure is Required");
			empty = true;
		}

		if(!empty) {
			try {
				Log.d("Emptyness", "Not Empty");
				// Spread the fields from profileData
				String profileData = loanApplicationJson.optString("profileData", "");
				if (!profileData.isEmpty()) {
					JSONObject profileJson = new JSONObject(profileData);

					// Iterate through the fields in profileData and add them to finalObject
					Iterator<String> keys = profileJson.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						// Check if the key is not "__v" or "_id" before adding it to requestData
						if (!key.equals("__v") && !key.equals("_id")) {
							requestData.put(key, profileJson.get(key));
						}
					}
				}
				// Add the newly added fields to the final object
				requestData.put("coApplicantName", nameS);
				requestData.put("coApplicantGender", gender);
				requestData.put("coApplicantDob",dobS);
				requestData.put("coApplicantAge", ageS);
				requestData.put("relationship", relationshipS);
				requestData.put("landHolding", landHoldingS);
				requestData.put("landHoldingType", landTypeS);
				requestData.put("monthlyHHIncome", incomeS);
				requestData.put("monthlyHHExpenses", expensesS);
				requestData.put("requestedAmount", amountS);
				requestData.put("purpose", purposeS);
				requestData.put("loanTenure", tenureS);
				requestData.put("fpoName",fpoNameS);
				requestData.put("id",idS);
				requestData.put("loanWindowId",windowIdS);
				requestData.put("fpoId", fpoIdS);
				requestData.put("loanId", loanIdS);
				requestData.put("intrest",interestS);


			}
			catch (JSONException e) {
				Log.d("Loan Application Error", "can't Apply Loan");
				e.printStackTrace();
			}
			Log.d("Request Data", requestData.toString());

			// applyForLoan(context.getApplicationContext());
			loanApply(requestData);
		}
	}

	private static void applyForLoan(Context context) {
		String url = context.getString(R.string.url) + "/loanwindow/+" + idS + "/loan";
		String token = LoginActivity.getToken(context);

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
								JSONArray jsonArray = jsonResponse.getJSONArray("data");

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

	private static void loanApply(JSONObject requestData) {
		String url = context.getString(R.string.url) + "/loanwindow/" + idS + "/loan";
		Log.d("API URL", url);
		String token = LoginActivity.getToken(context);

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
		Request request = new Request.Builder()
				.url(url)
				.header("Authorization", "Bearer " + token)
				.post(requestBody)
				.build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.e("TAG", "Request failed: " + e.getMessage()); // Log the error
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				try {
					if (response.isSuccessful()) {
						String responseBody = response.body().string();
						Log.d("TAG", "Response: " + responseBody); // Log the response
						// Handle the response here if needed
					} else {
						String errorBody = response.body() != null ? response.body().string() : "No error body";
						String errorMessage = response.message();
						Log.e("TAG", "Request not successful: " + response.code() + " - " + errorMessage + " - " + errorBody); // Log the error response
					}
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} finally {
					// Make sure to close the response to release resources
					if (response.body() != null) {
						response.body().close();
					}
				}
			}
		});
	}

	private void addOnClickListenerForDOBButton()
	{
		dateOfBirth.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

			}
		});
	}

	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, month);
			myCalendar.set(Calendar.DAY_OF_MONTH, day);
			updateLabel(dateOfBirth);
		}
	};

	private void updateLabel(EditText text)
	{
		String myFormat = "MM/dd/yy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
		text.setText(dateFormat.format(myCalendar.getTime()));
	}

	private void createUniqueID() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "").substring(0, 10);
		loanID.setText(id);
		loanIdS = id;
	}

	private void getInterestRate() {
		// ?fpoId=" + LoginActivity.getFpoID(getContext());
		String url = getContext().getString(R.string.url) +  "/loanwindow/activeLoanWindow/farmer";
		String token = LoginActivity.getToken(getContext());
		fpoIdS = LoginActivity.getFpoID(getContext());

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
								Log.d("Interest", responseBody);
								JSONObject jsonResponse = new JSONObject(responseBody);
								interest.setText(String.valueOf(jsonResponse.getInt("fpoInterestRate")));
								interestS = String.valueOf(jsonResponse.getInt("fpoInterestRate"));
								JSONArray data = jsonResponse.getJSONArray("data");
								if(data.length() > 0) {
									JSONObject firstItem = data.getJSONObject(0);
									idS = firstItem.getString("id");
									windowIdS = firstItem.getString("windowId");
									fpoNameS = firstItem.getString("fpoName");

								}


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

}
