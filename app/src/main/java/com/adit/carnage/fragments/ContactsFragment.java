package com.adit.carnage.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adit.carnage.R;
import com.adit.carnage.adapters.MoviesAdapter;
import com.adit.carnage.apis.classes.Movie;
import com.adit.carnage.classes.Constants;
import com.adit.carnage.interfaces.HomeView;
import com.adit.carnage.presenters.MainPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsFragment extends Fragment implements HomeView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MainPresenter presenter;

    @BindView(R.id.rv)
    RecyclerView rv;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this, getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        rv.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvContacts = view.findViewById(R.id.tvContacts);
        presenter.initSharedPreferences();
        presenter.initApi();
        presenter.fetchNowPlaying(Constants.LANGUAGE, 1, null);
    }

    public void initAdapter(List<Movie> list){
//        Toast.makeText(getContext(), "Setting adapter...", Toast.LENGTH_SHORT).show();
        mAdapter = new MoviesAdapter(list, getContext());
        rv.setAdapter(mAdapter);
    }

    @Override
    public void setMoviesAdapter(List<Movie> list) {
//        Toast.makeText(getContext(), "setMovieAdapter event occured", Toast.LENGTH_SHORT).show();
        initAdapter(list);
    }

    @OnClick(R.id.btnLogout)
    public void logout(){
        presenter.destroy();
    }
}
