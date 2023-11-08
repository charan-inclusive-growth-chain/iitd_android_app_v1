package com.example.appv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoanApplication extends AppCompatActivity
{
	List<Fragment> fragmentOrder = Arrays.asList(new LoanAppForm1(), new LoanAppForm2(), new LoanAppForm3(), new LoanAppForm4());
	//List<Fragment> fragmentOrder = Arrays.asList(new LoanAppForm1(), new LoanAppForm2(), new LoanAppForm3(), new LoanAppForm4());
	int currentFragmentNumber = 0;

	Button next, prev, submit;
	String id;
	LinearLayout nextLayout, prevLayout, submitLayout;

	public static JSONObject loanObject = new JSONObject();

	public static JSONObject loanApplicationJson = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loan_application_activity);

		addOnClickListenerForNextButton();
		addOnClickListenerForPrevButton();
		addOnClickListenerForSubmitButton();
		resetButtons();
		handleNextFragments(fragmentOrder.get(currentFragmentNumber));

	}
	public static JSONObject getLoanApplicationJson() {
		return loanApplicationJson;
	}

	private void addOnClickListenerForSubmitButton()
	{
		submitLayout = findViewById(R.id.loan_app_submit_layout);
		submit = findViewById(R.id.loan_app_submit);
		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				new AlertDialog.Builder(LoanApplication.this)
					.setTitle("Farmer Loan")
					.setMessage("Do you want to apply this loan?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							LoanAppForm4.submit(submit);
							finish();
						}
					}).setNegativeButton("No", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{

						}
					}).show();
			}
		});
	}

	private void addOnClickListenerForPrevButton()
	{
		prevLayout = findViewById(R.id.loan_app_prev_layout);
		prev = findViewById(R.id.loan_app_previous);
		prev.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				currentFragmentNumber--;
				resetButtons();
				handlePreviousFragments(fragmentOrder.get(currentFragmentNumber));
			}
		});

	}

	private void addOnClickListenerForNextButton()
	{
		nextLayout = findViewById(R.id.loan_app_next_layout);
		next = findViewById(R.id.loan_app_next);
		next.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
				currentFragmentNumber++;
				resetButtons();
				handleNextFragments(fragmentOrder.get(currentFragmentNumber));
			}
		});
	}

	private void resetButtons()
	{
		if(currentFragmentNumber == fragmentOrder.size() - 1)
		{
			nextLayout.setVisibility(View.GONE);
			submitLayout.setVisibility(View.VISIBLE);
		}
		else if(currentFragmentNumber == 0)
		{
			prevLayout.setVisibility(View.GONE);
			submitLayout.setVisibility(View.GONE);
		}
		else
		{
			prevLayout.setVisibility(View.VISIBLE);
			nextLayout.setVisibility(View.VISIBLE);
			submitLayout.setVisibility(View.GONE);
		}
	}

	private void handleNextFragments(Fragment fragment)
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
		transaction.replace(R.id.loan_app_fragment_ph, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void handlePreviousFragments(Fragment fragment)
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
		transaction.replace(R.id.loan_app_fragment_ph, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}


	@Override public void onBackPressed()
	{
		if(currentFragmentNumber == 0)
		{
			finish();
		}
		super.onBackPressed();
	}
}
