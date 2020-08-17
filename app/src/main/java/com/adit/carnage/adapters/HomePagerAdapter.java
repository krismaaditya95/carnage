package com.adit.carnage.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.adit.carnage.fragments.ChatFragment;
import com.adit.carnage.fragments.ContactsFragment;
import com.adit.carnage.fragments.FragmentDua;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // exerc00
//        Fragment fragment = new FragmentDua();
//        Bundle args = new Bundle();
//        args.putInt(FragmentDua.ARG_OBJECT, position+1);
//        fragment.setArguments(args);
//        return fragment;

        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = ChatFragment.newInstance();
                break;
            case 1:
                fragment = FragmentDua.newInstance();
                break;
            case 2:
                fragment = ContactsFragment.newInstance();
                break;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        String title = "";

        switch(position){
            case 0:
                title = "Video Recorder";
                break;
            case 1:
                title = "Tracker";
                break;
            case 2:
                title = "Locked";
                break;
        }

        //return "HALAMAN " + (position + 1);
        return title;
    }
}
