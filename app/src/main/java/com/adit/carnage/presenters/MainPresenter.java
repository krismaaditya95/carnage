package com.adit.carnage.presenters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.adit.carnage.activities.LoginActivity;
import com.adit.carnage.apis.ApiClient;
import com.adit.carnage.apis.classes.Movie;
import com.adit.carnage.apis.responses.MovieResponse;
import com.adit.carnage.interfaces.HomeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends BasePresenter{

    private final static String LANGUAGE = "en-US";

    ApiClient client;
    //ApiInterface apiInterface;
    private HomeView homeView;

    public MainPresenter(HomeView homeView, Activity activity){
        this.homeView = homeView;
        this.activity = activity;
    }

    public void checkLogin(){
        if(checkLoginState() == false){
            // user belum login
            Toast.makeText(activity.getApplicationContext(), "Anda belum login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }else{
            Toast.makeText(activity.getApplicationContext(), "Anda sudah login", Toast.LENGTH_SHORT).show();
            initApi();
            //fetchPopularMovies(LANGUAGE, 1 , null);
//            fetchNowPlaying(LANGUAGE, 1, null);
        }
    }

    public void destroy(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        checkLogin();
    }

    public void fetchPopularMovies(String language, Integer page, String region){
        if(apiInterface != null){
            Call<MovieResponse> response = apiInterface.getPopularMovies(client.apiKey, language, page, region);
            response.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if(response.isSuccessful()){
                        List<Movie> list = response.body().getResults();
                        //Toast.makeText(activity, "API CALL SUKSES : " + "\n" + list.get(0).getRelease_date(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, "API CALL SUKSES", Toast.LENGTH_SHORT).show();
                        if(list.size() > 0){
                            homeView.setMoviesAdapter(list);
                        }else{
                            Toast.makeText(activity, "MOVIE LIST NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(activity, "apiInterface NULL!", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchNowPlaying(String language, Integer page, String region){
        if(apiInterface != null){
            Call<MovieResponse> response = apiInterface.getNowPlaying(client.apiKey, language, page, region);
            response.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if(response.isSuccessful()){
                        List<Movie> list = response.body().getResults();
                        Toast.makeText(activity, "API CALL SUKSES", Toast.LENGTH_SHORT).show();
                        if(list.size() > 0){
                            Toast.makeText(activity, "Movie 1 : " + list.get(0).getOriginal_title(), Toast.LENGTH_SHORT).show();
                            homeView.setMoviesAdapter(list);
                        }else{
                            Toast.makeText(activity, "MOVIE LIST NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(activity, "API CALL GAGAL", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(activity, "apiInterface NULL!", Toast.LENGTH_SHORT).show();
        }
    }
}
