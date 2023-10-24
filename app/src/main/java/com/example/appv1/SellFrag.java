package com.example.appv1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SellFrag newInstance(String param1, String param2) {
        SellFrag fragment = new SellFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        // Initialize your ViewPager2 and TabLayout here
        ViewPager2 viewPager = view.findViewById(R.id.viewpager2);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Create an adapter for the ViewPager2
        VPAdapter vpAdapter = new VPAdapter(this);
        vpAdapter.addFragment(new SellToFPOTabFrag(), "Sell To FPO");
        vpAdapter.addFragment(new EnterProduceTabFrag(), "Enter Produce");

        // Set the adapter for the ViewPager2
        viewPager.setAdapter(vpAdapter);

        // Attach the TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(vpAdapter.getFragmentTitle(position));
        }).attach();


        return view;
    }
}