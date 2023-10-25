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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class RegisterAddressDetailsFragment extends Fragment
{
    EditText doorNumber, streetName, village, taluk, pincode;
    Spinner district, state;
    TextView districtT, stateT;
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
        village = getView().findViewById(R.id.register_Village);
        taluk = getView().findViewById(R.id.register_taluk);
        district = getView().findViewById(R.id.register_district);
        state = getView().findViewById(R.id.register_state);
        districtT = getView().findViewById(R.id.register_district_text);
        stateT = getView().findViewById(R.id.register_state_text);
        pincode = getView().findViewById(R.id.register_pincode);
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
            doorNumber.setError("Enter door number");
            return false;
        } else {
            doorNumber.setError(null);
        }

        if (streetS.isEmpty()) {
            streetName.setError("Enter street name");
            return false;
        } else if(!streetS.matches("^[A-Za-z0-9 ]+$")) {
            streetName.setError("Incorrect format");
            return false;
        }else {
            streetName.setError(null);
        }

        if (villageS.isEmpty()) {
            village.setError("Enter vilage");
            return false;
        } else if(!villageS.matches("^[A-Za-z ]+$")) {
            village.setError("Only alpha allowed");
            return false;
        }else {
            village.setError(null);
        }

        if (talukS.isEmpty()) {
            taluk.setError("Enter taluk");
            return false;
        } else if(!talukS.matches("^[A-Za-z ]+$")) {
            taluk.setError("Only alpha allowed");
            return false;
        }else {
            taluk.setError(null);
        }

        if (districtS.equals("Select a District")) {
            districtT.setError("Enter district");
            return false;
        } else {
            districtT.setError(null);
        }

        if (stateS.equals("Select a State")) {
            stateT.setError("Enter state");
            return false;
        } else {
            stateT.setError(null);
        }

        if (pinS.isEmpty()) {
            pincode.setError("Enter pin code");
            return false;
        } else if(!pinS.matches("\\d{6}")) {
            pincode.setError("Enter a valid pin code");
            return false;
        } else {
            pincode.setError(null);
        }

        try {
            registerAsFarmerJson.put("doorNumber", doorS);
            registerAsFarmerJson.put("street", streetS);
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
