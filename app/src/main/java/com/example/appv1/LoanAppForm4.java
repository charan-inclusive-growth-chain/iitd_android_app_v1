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
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
	TextView dobT;
	static TextView nameT;
	static TextView ageT;
	static TextView relationshipT;
	static TextView landHoldingT;
	static TextView typeoflandHoldingT;
	static TextView incomeT;
	static TextView expensesT;
	static TextView amountT;
	TextView purposeT;
	TextView tenureT;
	TextView interestT;
	TextView idText;
	RadioButton male, female;
	RadioGroup genderRadioGroup;
	String gender;
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
		purposeT = getView().findViewById(R.id.loan_purpose_text);
		tenureT = getView().findViewById(R.id.loan_tenure_text);


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
		submitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				boolean empty = false;

				if(applicantName != null) {
					String nameS = applicantName.getText().toString().trim();

				} else {
					nameT.setError("Enter co-applicant name");
					empty = true;
				}

				if(dateOfBirth != null) {
					String dobS = dateOfBirth.getText().toString().trim();
				} else {
					ageT.setError("Enter age");
					empty = true;
				}

				if(age != null) {
					String dobS = dateOfBirth.getText().toString().trim();
				} else {
					ageT.setError("Enter age");
					empty = true;
				}

				if(relationship != null) {
					String relationshipS = relationship.getText().toString().trim();
				} else {
					relationshipT.setError("Enter relationship");
					empty = true;
				}

				if(landHolding != null) {
					String landHoldingS = landHolding.getText().toString().trim();
				} else {
					landHoldingT.setError("Enter land holding");
					empty = true;
				}

				if(typeOfLandHolding != null) {
					String landTypeS = typeOfLandHolding.getText().toString().trim();
				} else {
					typeoflandHoldingT.setError("Enter land holding type");
					empty = true;
				}

				if(monthlyHHIncome != null) {
					String incomeS = monthlyHHIncome.getText().toString().trim();
				} else {
					incomeT.setError("Enter monthly hh income");
					empty = true;
				}

				if(monthlyHHIncome != null) {
					String expensesS = monthlyHHExpenses.getText().toString().trim();
				} else {
					expensesT.setError("Enter monthly hh expenses");
					empty = true;
				}

				if(requestedAmount != null) {
					String amountS = requestedAmount.getText().toString().trim();
				} else {
					amountT.setError("Enter requested amount");
					empty = true;
				}

				if(empty == false) {
					try {
						// Spread the fields from profileData
						String profileData = loanApplicationJson.optString("profileData", "");
						if (!profileData.isEmpty()) {
							JSONObject profileJson = new JSONObject(profileData);

							// Iterate through the fields in profileData and add them to finalObject
							Iterator<String> keys = profileJson.keys();
							while (keys.hasNext()) {
								String key = keys.next();
								requestData.put(key, profileJson.get(key));
							}
						}
						// Add the newly added fields to the final object
						requestData.put("applicantName", applicantName.getText().toString());
						requestData.put("age", age.getText().toString());
						requestData.put("relationship", relationship.getText().toString());
						requestData.put("landHolding", landHolding.getText().toString());
						requestData.put("typeOfLandHolding", typeOfLandHolding.getText().toString());
						requestData.put("monthlyHHIncome", monthlyHHIncome.getText().toString());
						requestData.put("monthlyHHExpenses", monthlyHHExpenses.getText().toString());
						requestData.put("requestedAmount", requestedAmount.getText().toString());



					}
					catch (JSONException e) {
						Log.d("Loan Application Error", "can't Apply Loan");
						e.printStackTrace();
					}
					Log.d("Request Data", requestData.toString());

					// applyForLoan(context.getApplicationContext());
				}
			}
		});
	}

	private static void applyForLoan(Context context) {
		String url = context.getString(R.string.url) + "/loanwindow/activeLoanWindow/farmer?fpoId=" + LoginActivity.getFpoID(context);
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
	}

	private void getInterestRate() {
		String url = getContext().getString(R.string.url) +  "/loanwindow/activeLoanWindow/farmer?fpoId=" + LoginActivity.getFpoID(getContext());
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
								Log.d("Interest", responseBody);
								JSONObject jsonResponse = new JSONObject(responseBody);
								interest.setText(String.valueOf(jsonResponse.getInt("fpoInterestRate")));
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
