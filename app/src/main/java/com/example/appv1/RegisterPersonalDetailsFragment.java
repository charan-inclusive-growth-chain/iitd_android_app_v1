package com.example.appv1;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.appv1.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterPersonalDetailsFragment extends Fragment
{
    final Calendar myCalendar = Calendar.getInstance();
    EditText dateOfBirth, name, mobile, password, confirmPassword, fatherName, motherName, occupation;
    EditText education, nop, residence, caste, religion;
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
        addOnClickListenerForDOBButton();
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
            name.setError("Enter name");
            return false;
        } else if (nameS.contains(" ")) {
            name.setError("Name should not contain spaces");
            return false;
        } else if (!nameS.equals(nameS.toLowerCase())) {
            name.setError("Name should be lowercase");
            return false;
        } else {
            name.setError(null);
        }

        if (mobileS.isEmpty()) {
            mobile.setError("Enter mobile number");
            return false;
        } else if (!mobileS.matches("\\d{10}")) {
            mobile.setError("Invalid mobile number");
            return false;
        } else {
            mobile.setError(null);
        }

        if (gender.isEmpty()) {
            male.setError("Enter gender");
            return false;
        } else {
            male.setError(null);
        }

        if (dobS.isEmpty()) {
            dateOfBirth.setError("Select DOB");
            return false;
        } else {
            dateOfBirth.setError(null);
        }

        if (passwordS.isEmpty()) {
            password.setError("Enter password");
            return false;
        } else {
            password.setError(null);
        }

        if (confirmS.isEmpty()) {
            confirmPassword.setError("Enter confirm password");
            return false;
        } else if (!confirmS.equals(passwordS)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        } else {
            confirmPassword.setError(null);
        }

        if (fatherS.isEmpty()) {
            fatherName.setError("Enter father's name");
            return false;
        } else if(!fatherS.matches("^[A-Za-z ]+$")) {
            fatherName.setError("Only alpha allowed");
            return false;
        }else {
            fatherName.setError(null);
        }

        if (motherS.isEmpty()) {
            motherName.setError("Enter mother's name");
            return false;
        } else if(!motherS.matches("^[A-Za-z ]+$")) {
            motherName.setError("Only alpha allowed");
            return false;
        }else {
            motherName.setError(null);
        }

        if (occupationS.isEmpty()) {
            occupation.setError("Enter occupation");
            return false;
        } else if(!occupationS.matches("^[A-Za-z ]+$")) {
            occupation.setError("Only alpha allowed");
            return false;
        }else {
            occupation.setError(null);
        }

        if (educationS.isEmpty()) {
            education.setError("Enter education");
            return false;
        } else {
            education.setError(null);
        }

        if (nopS.isEmpty()) {
            nop.setError("Enter nature of place");
            return false;
        } else if(!nopS.matches("^[A-Za-z ]+$")) {
            nop.setError("Only alpha allowed");
            return false;
        }else {
            nop.setError(null);
        }

        if (residenceS.isEmpty()) {
            residence.setError("Enter residence");
            return false;
        }else if(!residenceS.matches("^[A-Za-z ]+$")) {
            residence.setError("Only alpha allowed");
            return false;
        } else {
            residence.setError(null);
        }

        if (casteS.isEmpty()) {
            caste.setError("Enter caste");
            return false;
        } else if(!casteS.matches("^[A-Za-z ]+$")) {
            caste.setError("Only alpha allowed");
            return false;
        }else {
            caste.setError(null);
        }

        if (religionS.isEmpty()) {
            religion.setError("Enter religion");
            return false;
        } else if(!religionS.matches("^[A-Za-z ]+$")) {
            religion.setError("Only alpha allowed");
            return false;
        }else {
            religion.setError(null);
        }

        try {
            registerAsFarmerJson.put("userName", nameS);
            registerAsFarmerJson.put("mspId", "Org1MSP");
            registerAsFarmerJson.put("type", "farmer");
            registerAsFarmerJson.put("mobile", mobileS);
            registerAsFarmerJson.put("password", passwordS);
            registerAsFarmerJson.put("confirmPassword", confirmS);
            registerAsFarmerJson.put("dateOfBirth", dobS);
            registerAsFarmerJson.put("age", ageS);
            registerAsFarmerJson.put("gender", gender);
            registerAsFarmerJson.put("fatherName", fatherS);
            registerAsFarmerJson.put("motherName", motherS);
            registerAsFarmerJson.put("occupation", occupationS);
            registerAsFarmerJson.put("education", educationS);
            registerAsFarmerJson.put("natureOfPlace", nopS);
            registerAsFarmerJson.put("residence", residenceS);
            registerAsFarmerJson.put("caste", casteS);
            registerAsFarmerJson.put("religion", religionS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
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

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int selectedYear = myCalendar.get(Calendar.YEAR);
        int selectedMonth = myCalendar.get(Calendar.MONTH);
        int selectedDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        age = currentYear - selectedYear;
        if (currentMonth < selectedMonth || (currentMonth == selectedMonth && currentDay < selectedDay)) {
            age--;
        }
    }

}
