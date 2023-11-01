package com.example.appv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CropAdvisoryFrag extends Fragment
{

	public CropAdvisoryFrag()
	{
		// Required empty public constructor
	}

	public static CropAdvisoryFrag newInstance()
	{
		CropAdvisoryFrag fragment = new CropAdvisoryFrag();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
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
		return inflater.inflate(R.layout.crop_advisory_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadTrainingData();
	}

	private void loadTrainingData() {
		String url = getContext().getString(R.string.url) + "/nisa/crop-advisory";
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
		LinearLayout updatesContainer = getView().findViewById(R.id.update_crop_container);

		for (int i = 0; i < dataArray.length(); i++) {
			try {
				JSONObject trainingObj = dataArray.getJSONObject(i);

				String title = trainingObj.getString("title");
				String content = trainingObj.getString("content");
				String createdAt = trainingObj.getString("createdAt");
				String updatedAt = trainingObj.getString("updatedAt");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				Date createdDate = dateFormat.parse(createdAt);
				SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
				String createdAtFormatted = displayFormat.format(createdDate);

				Date updatedDate = dateFormat.parse(updatedAt);
				String updatedAtFormatted = displayFormat.format(updatedDate);


				View cardView = LayoutInflater.from(getContext()).inflate(R.layout.crop_advisory_card_layout, null, false);
				TextView titleTextView = cardView.findViewById(R.id.title);
				TextView contentTextView = cardView.findViewById(R.id.content);
				contentTextView.setMaxLines(3);
				TextView createdAtTextView = cardView.findViewById(R.id.created_at);
				TextView updatedAtTextView = cardView.findViewById(R.id.updated_at);

				TextView readMore = cardView.findViewById(R.id.read_more);
				readMore.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setTitle(title);
						builder.setMessage(content);
						builder.setPositiveButton("Close", null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});

				titleTextView.setText(title);
				contentTextView.setText(content);
				createdAtTextView.setText("Created: "+ createdAtFormatted);
				updatedAtTextView.setText("Updated: " + updatedAtFormatted);

				updatesContainer.addView(cardView);
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
