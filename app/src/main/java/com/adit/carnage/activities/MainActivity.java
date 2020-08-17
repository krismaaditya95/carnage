package com.adit.carnage.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.adit.carnage.R;
import com.adit.carnage.adapters.HomePagerAdapter;
import com.adit.carnage.fragments.HostFragment;

import io.reactivex.annotations.NonNull;

public class MainActivity extends BaseActivity implements HostFragment.OnFragmentInteractionListener {

    HostFragment fragment = new HostFragment();
    HomePagerAdapter homePagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle args = new Bundle();
        args.putString("param1", "Hello");
        args.putString("param2", "Motherfucker");

        if(findViewById(R.id.container) != null){
            if(savedInstanceState != null){
                return;
            }

//            FragmentSatu fragment = new FragmentSatu();
//            fragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

//            FragmentSatu.show(this, args);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.videoCall:
                showDialog("Info", "VideoCall dipilih");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
