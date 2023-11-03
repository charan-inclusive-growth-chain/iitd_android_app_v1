package com.example.appv1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class LoanApplicationDetailsActivity extends AppCompatActivity {
    TextInputEditText bank, account, ifsc, branch, username, gender, mobile, dob, age, aadharNo, panNo, fName, mName, doorNo, street, village;
    TextInputEditText taluk, district, state, pin, occupation, nop, residence, caste, religion, coName, coGender, coDob, coAge, relationship, landHolding;
    TextInputEditText type, income, expense, amount, purpose, tenure, interest, id;
    Button aadhar, pan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_application_details);


        bank = findViewById(R.id.bank_name);
        account = findViewById(R.id.account_no);
        ifsc = findViewById(R.id.ifsc);
        branch = findViewById(R.id.branch_name);
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        mobile = findViewById(R.id.mobile);
        dob = findViewById(R.id.dob);
        age = findViewById(R.id.age);
        aadharNo = findViewById(R.id.aadhar_no);
        aadhar = findViewById(R.id.aadhar_button);
        panNo = findViewById(R.id.pan_no);
        pan = findViewById(R.id.pan_button);
        fName = findViewById(R.id.father_name);
        mName = findViewById(R.id.mother_name);
        doorNo = findViewById(R.id.door_no);
        street = findViewById(R.id.street);
        village = findViewById(R.id.village);
        taluk = findViewById(R.id.taluk);
        district = findViewById(R.id.district);
        state = findViewById(R.id.state);
        pin = findViewById(R.id.pin_code);
        occupation = findViewById(R.id.occupation);
        nop = findViewById(R.id.nop);
        residence = findViewById(R.id.residence);
        caste = findViewById(R.id.caste);
        religion = findViewById(R.id.religion);
        coName = findViewById(R.id.co_name);
        coGender = findViewById(R.id.co_gender);
        coDob = findViewById(R.id.co_dob);
        coAge = findViewById(R.id.co_age);
        relationship = findViewById(R.id.relationship);
        landHolding = findViewById(R.id.land_holding);
        type = findViewById(R.id.type);
        income = findViewById(R.id.income);
        expense = findViewById(R.id.expense);
        amount = findViewById(R.id.requested_amount);
        purpose = findViewById(R.id.purpose);
        tenure = findViewById(R.id.tenure);
        interest = findViewById(R.id.interest);
        id = findViewById(R.id.loan_id);

        Intent intent = getIntent();
        if (intent.hasExtra("loanObject")) {
            try {
                String loanObjectString = intent.getStringExtra("loanObject");
                Log.d("Loan Object", loanObjectString);
                JSONObject loanObject = new JSONObject(loanObjectString);

                bank.setText(loanObject.getString("bankName"));
                account.setText(loanObject.getString("accountNumber"));
                ifsc.setText(loanObject.getString("ifscCode"));
                branch.setText(loanObject.getString("branchName"));
                username.setText(loanObject.getString("name"));
                gender.setText(loanObject.getString("gender"));
                mobile.setText(loanObject.getString("mobile"));
                dob.setText(loanObject.getString("dob").substring(0, 10));
                age.setText(String.valueOf(loanObject.getInt("age")));
                aadharNo.setText(loanObject.getString("aadharCardNumber"));
                panNo.setText(loanObject.getString("panCardNumber"));
                fName.setText(loanObject.getString("fatherName"));
                mName.setText(loanObject.getString("motherName"));
                doorNo.setText(loanObject.getString("doorNumber"));
                street.setText(loanObject.getString("street"));
                village.setText(loanObject.getString("village"));
                taluk.setText(loanObject.getString("taluk"));
                district.setText(loanObject.getString("district"));
                state.setText(loanObject.getString("state"));
                pin.setText(loanObject.getString("pinCode"));
                occupation.setText(loanObject.getString("occupation"));
                nop.setText(loanObject.getString("natureOfPlace"));
                residence.setText(loanObject.getString("residence"));
                caste.setText(loanObject.getString("caste"));
                religion.setText(loanObject.getString("religion"));
                coName.setText(loanObject.getString("coApplicantName"));
                coGender.setText(loanObject.getString("coApplicantGender"));
                coDob.setText(loanObject.getString("coApplicantDob").substring(0, 10));
                coAge.setText(String.valueOf(loanObject.getInt("coApplicantAge")));
                relationship.setText(loanObject.getString("relationship"));
                landHolding.setText(loanObject.getString("landHolding"));
                type.setText(loanObject.getString("landHoldingType"));
                income.setText(loanObject.getString("monthlyHHIncome"));
                expense.setText(loanObject.getString("monthlyHHExpenses"));
                amount.setText("₹" + loanObject.getString("requestedAmount"));
                purpose.setText(loanObject.getString("purpose"));
                tenure.setText(String.valueOf(loanObject.getInt("loanTenure")));
                interest.setText(loanObject.isNull("intrest") ? "" : loanObject.getString("intrest") + "%");
                id.setText(loanObject.getString("loanId"));

                aadhar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aadharImageUrl = loanObject.optString("aadharCardImage", "");
                        if (!aadharImageUrl.isEmpty()) {
                            showImagePopup(aadharImageUrl);
                        }
                    }
                });

                pan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String panImageUrl = loanObject.optString("panCardImage", "");
                        if (!panImageUrl.isEmpty()) {
                            showImagePopup(panImageUrl);
                        }
                    }
                });


            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    PopupWindow imagePopup;

    private void showImagePopup(String imageUrl) {
        final Dialog dialog = new Dialog(LoanApplicationDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_image);
        ImageView imageView = dialog.findViewById(R.id.popupImageView);

        /// Load and display the image using Picasso
        Picasso.get()
                .load(imageUrl)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Image", "Loaded Succesfully");
                        // Image loaded successfully
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error (e.g., log the error)
                        e.printStackTrace();
                    }
                });


        dialog.show();
    }
}
