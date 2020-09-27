package com.adit.carnage.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.adit.carnage.R;
import com.adit.carnage.adapters.HomePagerAdapter;
import com.adit.carnage.adapters.MoviesAdapter;
import com.adit.carnage.apis.classes.Movie;
import com.adit.carnage.fragments.HostFragment;
import com.adit.carnage.interfaces.HomeView;
import com.adit.carnage.presenters.MainPresenter;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class MainActivity extends BaseActivity implements HostFragment.OnFragmentInteractionListener, HomeView {

    HostFragment fragment = new HostFragment();
    HomePagerAdapter homePagerAdapter;
    ViewPager viewPager;
    MainPresenter presenter;

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
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this, this);
        presenter.initSharedPreferences();
        presenter.checkLogin();
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

    @Override
    public void setMoviesAdapter(List<Movie> list) {
        Toast.makeText(this, "setMovieAdapter event occured", Toast.LENGTH_SHORT).show();
    }
}
