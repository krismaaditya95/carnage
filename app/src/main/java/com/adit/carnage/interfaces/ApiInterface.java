package com.adit.carnage.interfaces;

import com.adit.carnage.apis.responses.ConfigurationResponse;
import com.adit.carnage.apis.responses.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String api_key,
                                         @Query("language") String language,
                                         @Query("page") Integer page,
                                         @Query("region") String region);

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlaying(@Query("api_key") String api_key,
                                      @Query("language") String language,
                                      @Query("page") Integer page,
                                      @Query("region") String region);

    @GET("configuration")
    Call<ConfigurationResponse> getImageConfiguration(@Query("api_key") String api_key);


}
