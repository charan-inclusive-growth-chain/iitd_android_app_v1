package com.example.appv1;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appv1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class RegisterAddressDetailsFragment extends Fragment
{
    TextInputEditText doorNumber, streetName, village, taluk, pincode;
    Spinner district, state;
    TextView districtT, stateT;
    TextInputLayout door, street, v, t, pin, d, st;
    JSONObject registerAsFarmerJson;

    public RegisterAddressDetailsFragment()
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
        return inflater.inflate(R.layout.register_address_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        doorNumber = getView().findViewById(R.id.register_door_number);
        streetName = getView().findViewById(R.id.register_street_name);
        village = getView().findViewById(R.id.register_village);
        taluk = getView().findViewById(R.id.register_taluk);
        district = getView().findViewById(R.id.register_district);
        state = getView().findViewById(R.id.register_state);
        pincode = getView().findViewById(R.id.register_pincode);

        door = getView().findViewById(R.id.reg_door_no);
        street = getView().findViewById(R.id.reg_street);
        v = getView().findViewById(R.id.reg_v);
        t = getView().findViewById(R.id.reg_t);
        d = getView().findViewById(R.id.reg_d);
        st = getView().findViewById(R.id.reg_st);
        pin = getView().findViewById(R.id.reg_pin);
        registerAsFarmerJson = RegisterActivity.getRegisterAsFarmerJson();
        fillStates();
    }

    public void fillStates() {
        List<String> states = Arrays.asList("Select a State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state.setAdapter(stateAdapter);

        int defaultStateIndex = states.indexOf("Jharkhand");
        state.setSelection(defaultStateIndex);

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedState = state.getSelectedItem().toString();
                selectedState = selectedState;

                if (!selectedState.equals("Jharkhand")) {
                    List<String> districts = Arrays.asList("Select a District");
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districts);
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(districtAdapter);
                } else {
                    // If the selected state is Jharkhand, populate the district spinner with the list of districts
                    List<String> districts = Arrays.asList("Select a District", "Dumka", "Godda", "Gumla", "Hazaribagh", "Jamtara", "Khunti", "Koderma", "Lohardaga", "Pakur", "Palamu", "Ranchi", "Sahebganj", "Simdega", "West Singhbhum");
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districts);
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district.setAdapter(districtAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public boolean checkFields() {
        String doorS = doorNumber.getText().toString().trim();
        String streetS = streetName.getText().toString().trim();
        String villageS = village.getText().toString().trim();
        String talukS = taluk.getText().toString().trim();
        String stateS = state.getSelectedItem().toString();
        String districtS = district.getSelectedItem().toString();
        String pinS = pincode.getText().toString().trim();

        if (doorS.isEmpty()) {
            door.setError("Enter door number");
            return false;
        } else {
            door.setError(null);
        }

        if (streetS.isEmpty()) {
            street.setError("Enter street name");
            return false;
        } else if(!streetS.matches("^[A-Za-z0-9 ]+$")) {
            street.setError("Incorrect format");
            return false;
        }else {
            street.setError(null);
        }

        if (villageS.isEmpty()) {
            v.setError("Enter vilage");
            return false;
        } else if(!villageS.matches("^[A-Za-z ]+$")) {
            v.setError("Only alpha allowed");
            return false;
        }else {
            v.setError(null);
        }

        if (talukS.isEmpty()) {
            t.setError("Enter taluk");
            return false;
        } else if(!talukS.matches("^[A-Za-z ]+$")) {
            t.setError("Only alpha allowed");
            return false;
        }else {
            t.setError(null);
        }

        if (districtS.equals("Select a District")) {
            d.setError("Enter district");
            return false;
        } else {
            d.setError(null);
        }

        if (stateS.equals("Select a State")) {
            st.setError("Enter state");
            return false;
        } else {
            st.setError(null);
        }

        if (pinS.isEmpty()) {
            pin.setError("Enter pin code");
            return false;
        } else if(!pinS.matches("\\d{6}")) {
            pin.setError("Enter a valid pin code");
            return false;
        } else {
            pin.setError(null);
        }

        try {
            registerAsFarmerJson.put("doorNumber", doorS);
            registerAsFarmerJson.put("streetName", streetS);
            registerAsFarmerJson.put("village", villageS);
            registerAsFarmerJson.put("taluk", talukS);
            registerAsFarmerJson.put("district", districtS);
            registerAsFarmerJson.put("state", stateS);
            registerAsFarmerJson.put("pinCode", pinS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }
}
