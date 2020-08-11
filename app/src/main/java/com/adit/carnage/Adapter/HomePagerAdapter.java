package com.adit.carnage.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.adit.carnage.Fragments.ChatFragment;
import com.adit.carnage.Fragments.ContactsFragment;
import com.adit.carnage.Fragments.FragmentDua;

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
                fragment = ChatFragment.newInstance("Ini halaman chat", "hehe");
                break;
            case 1:
                fragment = FragmentDua.newInstance("Ini halaman story", "haha");
                break;
            case 2:
                fragment = ContactsFragment.newInstance("Ini halaman kontak", "su");
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
        return "HALAMAN " + (position + 1);
    }
}
