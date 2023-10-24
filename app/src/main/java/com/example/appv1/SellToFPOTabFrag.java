package com.example.appv1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.AdapterView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.appv1.databinding.FragmentSellToFPOTabBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellToFPOTabFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellToFPOTabFrag extends Fragment {

    FragmentSellToFPOTabBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellToFPOTabFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellToFPOTabFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SellToFPOTabFrag newInstance(String param1, String param2) {
        SellToFPOTabFrag fragment = new SellToFPOTabFrag();
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
        binding = FragmentSellToFPOTabBinding.inflate(inflater,container,false);

        View view = binding.getRoot();
        String[] flowerName = {"Rose", "Lotus", "Lily", "Jasmine",
                "Tulip", "Orchid", "Levender", "RoseMarry", "Sunflower", "Carnation"};
        int[] flowerImages = {
                R.drawable.apples, R.drawable.brinjals, R.drawable.carrots, R.drawable.figs,
                R.drawable.guavas, R.drawable.lemons, R.drawable.mangoes,
                R.drawable.pineapples, R.drawable.strawberries, R.drawable.testimage
        };
        GridAdapter gridAdapter = new GridAdapter(requireActivity(), flowerName, flowerImages);
        binding.gridView.setAdapter(gridAdapter);

        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(requireContext(), "You Clicked on " + flowerName[position], Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }
}