package com.example.appv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrainingUpdatesFrag extends Fragment {

	public TrainingUpdatesFrag() {
		// Required empty public constructor
	}

	public static TrainingUpdatesFrag newInstance() {
		TrainingUpdatesFrag fragment = new TrainingUpdatesFrag();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.training_updates_fragment, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadTrainingData();
	}

	private void loadTrainingData() {
		String url = getContext().getString(R.string.url) + "/nisa/traning";
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
					String responseData = response.body().string();
					try {
						JSONObject responseObject = new JSONObject(responseData);
						JSONArray dataArray = responseObject.getJSONArray("data");

						requireActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								populateTrainingUpdates(dataArray);
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void populateTrainingUpdates(JSONArray dataArray) {
		LinearLayout updatesContainer = getView().findViewById(R.id.update_training_container);

		for (int i = 0; i < dataArray.length(); i++) {
			try {
				JSONObject trainingObj = dataArray.getJSONObject(i);

				String courseName = trainingObj.getString("courseName");
				String duration = trainingObj.getString("duration");
				String fee = trainingObj.getString("fee");
				String courseStart = trainingObj.getString("courseStartDate");
				String applyStart = trainingObj.getString("applicationStartDate");
				String applyEnd = trainingObj.getString("applicationEndDate");
				String remarks = trainingObj.getString("remarks");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dateFormat.parse(courseStart);
				SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMMM", Locale.US);
				String courseStartFormatted = displayFormat.format(date);

				Date date2 = dateFormat.parse(applyStart);
				SimpleDateFormat displayFormat2 = new SimpleDateFormat("dd MMMM", Locale.US);
				String applyStartFormatted = displayFormat.format(date2);

				Date date3 = dateFormat.parse(applyStart);
				SimpleDateFormat displayFormat3 = new SimpleDateFormat("dd MMMM", Locale.US);
				String applyEndFormatted = displayFormat.format(date3);

				View cardView = LayoutInflater.from(getContext()).inflate(R.layout.training_card_layout, null, false);
				TextView courseNameTextView = cardView.findViewById(R.id.course_name_textview);
				TextView durationTextView = cardView.findViewById(R.id.duration_textview);
				TextView feeTextView = cardView.findViewById(R.id.fee_textview);
				TextView courseStartView = cardView.findViewById(R.id.course_start_date);
				TextView applyStartT = cardView.findViewById(R.id.apply_start_date);
				TextView applyEndT = cardView.findViewById(R.id.apply_end_date);
				TextView remarksT = cardView.findViewById(R.id.remarks);
				remarksT.setMaxLines(1);
				TextView readMore = cardView.findViewById(R.id.training_read_more);

				readMore.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setTitle("Remarks");
						builder.setMessage(remarks);
						builder.setPositiveButton("Close", null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});


				courseNameTextView.setText(courseName);
				durationTextView.setText(duration);
				feeTextView.setText("₹ " + fee);
				courseStartView.setText(courseStartFormatted);
				applyStartT.setText(applyStartFormatted);
				applyEndT.setText(applyEndFormatted);
				remarksT.setText(remarks);

				updatesContainer.addView(cardView);

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}
}