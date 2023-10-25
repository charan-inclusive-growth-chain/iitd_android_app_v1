package com.example.appv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appv1.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterBankDetailsFragment extends Fragment
{
    JSONObject registerAsFarmerJson;
    EditText bank, branch, account, ifsc;
    public RegisterBankDetailsFragment()
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
        return inflater.inflate(R.layout.register_bank_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        bank = getView().findViewById(R.id.register_bank_name);
        branch = getView().findViewById(R.id.register_branch_name);
        account = getView().findViewById(R.id.register_bank_account_number);
        ifsc = getView().findViewById(R.id.register_bank_ifsc);
        registerAsFarmerJson = RegisterActivity.getRegisterAsFarmerJson();
    }

    public boolean checkFields() {
        String bankS = bank.getText().toString().trim();
        String branchS = branch.getText().toString().trim();
        String accountS = account.getText().toString().trim();
        String ifscS = ifsc.getText().toString().trim();

        if (bankS.isEmpty()) {
            bank.setError("Enter door number");
            return false;
        } else if(!bankS.matches("^[A-Za-z0-9 ]+$")) {
            bank.setError("Incorrect format");
            return false;
        }else {
            bank.setError(null);
        }

        if (branchS.isEmpty()) {
            branch.setError("Enter branch name");
            return false;
        } else if(!branchS.matches("^[A-Za-z0-9 ]+$")) {
            branch.setError("Incorrect format");
            return false;
        }else {
            branch.setError(null);
        }

        if (accountS.isEmpty()) {
            account.setError("Enter account number");
            return false;
        } else if(!accountS.matches("^[0-9]+$")) {
            account.setError("Only numbers allowed");
            return false;
        }else {
            account.setError(null);
        }

        if (ifscS.isEmpty()) {
            ifsc.setError("Enter IFSC");
            return false;
        } else if(!ifscS.matches("^[A-Za-z0-9]+$")) {
            ifsc.setError("Incorrect format");
            return false;
        }else {
            ifsc.setError(null);
        }

        try {
            registerAsFarmerJson.put("bankName", bankS);
            registerAsFarmerJson.put("accountNumber", accountS);
            registerAsFarmerJson.put("ifscCode", ifscS);
            registerAsFarmerJson.put("branchName", branchS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}
