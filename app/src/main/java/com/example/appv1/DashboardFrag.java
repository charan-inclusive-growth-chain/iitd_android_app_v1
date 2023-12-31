package com.example.appv1;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.example.appv1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFrag extends Fragment {

    LinearLayout storeShortCut;
    LinearLayout loanShortCut;

    LinearLayout cropAdvisoryShortCut;
    LinearLayout trainingShortCut;

    SliderView sliderView;
    int[] images = {R.drawable.farmer_image_2, R.drawable.farmer_image_3, R.drawable.farmer_image_4, R.drawable.farmerimage};



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFrag newInstance(String param1, String param2) {
        DashboardFrag fragment = new DashboardFrag();
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
        View rootView =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        sliderView = rootView.findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        return rootView;


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        addStoreShortCut();
        addLoanShortcut();
        addCropShortcut();
        addTrainingShortCut();


    }

    private void addStoreShortCut()
    {
        storeShortCut = getView().findViewById(R.id.store_link);

        storeShortCut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
                navigationView.getMenu().getItem(1).setChecked(true);
                selectDrawerItem(R.id.menu_store);
            }
        });

    }



    private void addLoanShortcut()
    {
        loanShortCut = getView().findViewById(R.id.loan_link);

        loanShortCut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
                navigationView.getMenu().getItem(3).setChecked(true);
                selectDrawerItem(R.id.menu_loan);
            }
        });

    }





    private void addCropShortcut()
    {
        cropAdvisoryShortCut = getView().findViewById(R.id.crop_link);

        cropAdvisoryShortCut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
                navigationView.getMenu().getItem(5).setChecked(true);
                selectDrawerItem(R.id.menu_cropadvisory);
            }
        });

    }

    private void addTrainingShortCut()
    {
        trainingShortCut = getView().findViewById(R.id.training_link);

        trainingShortCut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
                navigationView.getMenu().getItem(6).setChecked(true);
                selectDrawerItem(R.id.menu_training);
            }
        });
    }

    public void selectDrawerItem(int item)
    {
        Fragment fragment = null;
        Class fragmentClass = null;
        boolean flag = true;

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameLayout);

        if (item == R.id.menu_store) {
            flag = (currentFragment instanceof StoreFrag) ? false : true;
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentClass = StoreFrag.class;
        } else if (item == R.id.menu_loan) {
            flag = (currentFragment instanceof LoanListFrag) ? false : true;
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentClass = LoanListFrag.class;
        } else if (item == R.id.menu_cropadvisory) {
            flag = (currentFragment instanceof LoanDetailTempFrag) ? false : true;
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentClass = LoanDetailTempFrag.class;
        } else if (item == R.id.menu_training) {
            flag = (currentFragment instanceof LoanListFrag) ? false : true;
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentClass = LoanListFrag.class;
        }
        else if(item == R.id.menu_profile) {
            flag = (currentFragment instanceof ProfileFrag) ? false : true;
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            fragmentClass = ProfileFrag.class;
        }


        try
        {
            fragment = (Fragment) fragmentClass.newInstance();

            if(flag)
            {
                transaction.replace(R.id.frameLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }


}