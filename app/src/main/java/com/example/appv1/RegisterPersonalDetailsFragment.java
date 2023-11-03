package com.example.appv1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.appv1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPersonalDetailsFragment extends Fragment
{
    final Calendar myCalendar = Calendar.getInstance();
    TextInputEditText dateOfBirth, name, mobile, password, confirmPassword, fatherName, motherName, occupation;
    TextInputEditText education, nop, residence, caste, religion;

    TextInputLayout dob, n, m, pass, confpass, fname, mname, occ, edu, nat, res, cas, rel;
    RadioButton male, female;
    RadioGroup genderRadioGroup;
    JSONObject registerAsFarmerJson;
    String gender = "";
    int age;


    public RegisterPersonalDetailsFragment()
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
        return inflater.inflate(R.layout.register_personal_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        dateOfBirth = getView().findViewById(R.id.register_dob);
        // Set an OnClickListener for the dateOfBirth view
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });


        name = getView().findViewById(R.id.register_name);
        mobile = getView().findViewById(R.id.register_mobile);
        male = getView().findViewById(R.id.radio_pirates);
        female = getView().findViewById(R.id.radio_ninjas);
        genderRadioGroup = getView().findViewById(R.id.gender_radio_group);
        password = getView().findViewById(R.id.register_password);
        confirmPassword = getView().findViewById(R.id.register_confirm_password);
        fatherName = getView().findViewById(R.id.register_fathers_name);
        motherName = getView().findViewById(R.id.register_mothers_name);
        occupation = getView().findViewById(R.id.register_occupation);
        education = getView().findViewById(R.id.register_education);
        nop = getView().findViewById(R.id.register_nop);
        residence = getView().findViewById(R.id.register_residence);
        caste = getView().findViewById(R.id.register_caste);
        religion = getView().findViewById(R.id.register_religion);

        dob = getView().findViewById(R.id.reg_dob);
        n = getView().findViewById(R.id.reg_name);
        m = getView().findViewById(R.id.reg_mobile);
        pass = getView().findViewById(R.id.reg_pass);
        confpass = getView().findViewById(R.id.reg_conf_pass);
        fname = getView().findViewById(R.id.reg_fname);
        mname = getView().findViewById(R.id.reg_mname);
        occ = getView().findViewById(R.id.reg_occ);
        edu = getView().findViewById(R.id.reg_education);
        nat = getView().findViewById(R.id.reg_nop);
        res = getView().findViewById(R.id.reg_res);
        cas = getView().findViewById(R.id.reg_caste);
        rel = getView().findViewById(R.id.reg_religion);

        registerAsFarmerJson = RegisterActivity.getRegisterAsFarmerJson();

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
    }
    public boolean checkFields() {
        String nameS = name.getText().toString().trim();
        String mobileS = mobile.getText().toString().trim();
        String dobS = dateOfBirth.getText().toString().trim();
        String passwordS = password.getText().toString().trim();
        String confirmS = confirmPassword.getText().toString().trim();
        String fatherS = fatherName.getText().toString().trim();
        String motherS = motherName.getText().toString().trim();
        String occupationS = occupation.getText().toString().trim();
        String educationS = education.getText().toString().trim();
        String nopS = nop.getText().toString().trim();
        String residenceS = residence.getText().toString().trim();
        String casteS = caste.getText().toString().trim();
        String religionS = religion.getText().toString().trim();
        String ageS = String.valueOf(age);

        if (nameS.isEmpty()) {
            n.setError("Enter name");
            return false;
        } else if (nameS.contains(" ")) {
            n.setError("Name should not contain spaces");
            return false;
        } else if (!nameS.equals(nameS.toLowerCase())) {
            n.setError("Name should be lowercase");
            return false;
        } else {
            n.setError(null);
        }

        if (mobileS.isEmpty()) {
            m.setError("Enter mobile number");
            return false;
        } else if (!mobileS.matches("\\d{10}")) {
            m.setError("Invalid mobile number");
            return false;
        } else {
            m.setError(null);
        }

        if (gender.isEmpty()) {
            male.setError("Enter gender");
            return false;
        } else {
            male.setError(null);
        }

        if (dobS.isEmpty()) {
            dob.setError("Select DOB");
            return false;
        } else {
            dob.setError(null);
        }

        if (passwordS.isEmpty()) {
            pass.setError("Enter password");
            return false;
        } else if (passwordS.length() < 6) {
            pass.setError("Password must be at least 6 characters");
            return false;
        } else {
            pass.setError(null);
        }


        if (confirmS.isEmpty()) {
            confpass.setError("Enter confirm password");
            return false;
        } else if (!confirmS.equals(passwordS)) {
            confpass.setError("Passwords do not match");
            return false;
        } else {
            confpass.setError(null);
        }

        if (fatherS.isEmpty()) {
            fname.setError("Enter father's name");
            return false;
        } else if(!fatherS.matches("^[A-Za-z ]+$")) {
            fname.setError("Only alpha allowed");
            return false;
        }else {
            fname.setError(null);
        }

        if (motherS.isEmpty()) {
            mname.setError("Enter mother's name");
            return false;
        } else if(!motherS.matches("^[A-Za-z ]+$")) {
            mname.setError("Only alpha allowed");
            return false;
        }else {
            mname.setError(null);
        }

        if (occupationS.isEmpty()) {
            occ.setError("Enter occupation");
            return false;
        } else if(!occupationS.matches("^[A-Za-z ]+$")) {
            occ.setError("Only alpha allowed");
            return false;
        }else {
            occ.setError(null);
        }

        if (educationS.isEmpty()) {
            edu.setError("Enter education");
            return false;
        } else {
            edu.setError(null);
        }

        if (nopS.isEmpty()) {
            nat.setError("Enter nature of place");
            return false;
        } else if(!nopS.matches("^[A-Za-z ]+$")) {
            nat.setError("Only alpha allowed");
            return false;
        }else {
            nat.setError(null);
        }

        if (residenceS.isEmpty()) {
            res.setError("Enter residence");
            return false;
        }else if(!residenceS.matches("^[A-Za-z ]+$")) {
            res.setError("Only alpha allowed");
            return false;
        } else {
            res.setError(null);
        }

        if (casteS.isEmpty()) {
            cas.setError("Enter caste");
            return false;
        } else if(!casteS.matches("^[A-Za-z ]+$")) {
            cas.setError("Only alpha allowed");
            return false;
        }else {
            cas.setError(null);
        }

        if (religionS.isEmpty()) {
            rel.setError("Enter religion");
            return false;
        } else if(!religionS.matches("^[A-Za-z ]+$")) {
            rel.setError("Only alpha allowed");
            return false;
        }else {
            rel.setError(null);
        }

        if(dobS.isEmpty()){
            dob.setError("Select DOB");
        }
        else {
            dob.setError(null);
        }

        try {
            registerAsFarmerJson.put("userName", nameS);
            registerAsFarmerJson.put("mspId", "Org1MSP");
            registerAsFarmerJson.put("type", "farmer");
            registerAsFarmerJson.put("contactNumber", mobileS);
            registerAsFarmerJson.put("password", passwordS);
            //registerAsFarmerJson.put("confirmPassword", confirmS);
            registerAsFarmerJson.put("DOB", dobS);
            registerAsFarmerJson.put("age", ageS);
            registerAsFarmerJson.put("gender", gender);
            registerAsFarmerJson.put("fathersName", fatherS);
            registerAsFarmerJson.put("mothersName", motherS);
            registerAsFarmerJson.put("occupation", occupationS);
            registerAsFarmerJson.put("education", educationS);
            registerAsFarmerJson.put("natureOfPlace", nopS);
            registerAsFarmerJson.put("residence", residenceS);
            registerAsFarmerJson.put("caste", casteS);
            registerAsFarmerJson.put("religion", religionS);

            Log.d("Personal Details", registerAsFarmerJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }



    public void showDatePicker(View view) {
        if (view.getId() == R.id.register_dob) {
            Calendar calendar = Calendar.getInstance(); // Get the current date
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

                    dateOfBirth.setText(selectedDate); // Set the selected date in the TextInputEditText

                    // Calculate age based on the date of birth
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    try {
                        Date date = sdf.parse(selectedDate);
                        Calendar dob = Calendar.getInstance();
                        dob.setTime(date);

                        Calendar today = Calendar.getInstance();
                        age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                        // If the birthdate has not occurred this year yet, subtract one year
                        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                            age--;
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day); // Initialize with the current date
            datePickerDialog.show();
        }
    }





}
