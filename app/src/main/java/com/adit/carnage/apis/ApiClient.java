package com.adit.carnage.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    Gson gson = new GsonBuilder().create();

    public final static String apiKey = "359adc96ea8d88c5d497eef88aaf3b0c";
    public final static String BASE_URL = "https://api.themoviedb.org/3/";
    public final static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w154";

    public String getApiKey(){
        return apiKey;
    }

    public static Retrofit getClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
