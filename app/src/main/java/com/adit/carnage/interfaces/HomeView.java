package com.adit.carnage.interfaces;

import com.adit.carnage.apis.classes.Movie;

import java.util.List;

public interface HomeView {
    void setMoviesAdapter(List<Movie> list);
}
