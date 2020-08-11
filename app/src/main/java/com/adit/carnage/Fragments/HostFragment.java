package com.adit.carnage.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adit.carnage.Activity2;
import com.adit.carnage.Adapter.HomePagerAdapter;
import com.adit.carnage.BaseActivity;
import com.adit.carnage.MapActivity;
import com.adit.carnage.R;
import com.google.android.material.tabs.TabLayout;

public class HostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HomePagerAdapter homePagerAdapter;
    ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public HostFragment() {
        // Required empty public constructor
    }

    public static void show(BaseActivity source, Bundle args){
        HostFragment fragment = new HostFragment();
        fragment.setArguments(args);
        source.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentSatu.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FragmentSatu newInstance(String param1, String param2) {
//        FragmentSatu fragment = new FragmentSatu();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        return inflater.inflate(R.layout.fragment_fragment_satu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button tombol = view.findViewById(R.id.tombol);
        Button tracker = view.findViewById(R.id.btnTracker);

        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity2.startActivity((BaseActivity) getActivity());
//                finish();
            }
        });

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.startActivity((BaseActivity) getActivity());
            }
        });

        TextView tvSatu = view.findViewById(R.id.tvSatu);
        TextView tvDua = view.findViewById(R.id.tvDua);
        TabLayout tab = view.findViewById(R.id.tab);

        tvSatu.setText(mParam1);
        tvDua.setText(mParam2);

        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(homePagerAdapter);
        tab.setupWithViewPager(viewPager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
