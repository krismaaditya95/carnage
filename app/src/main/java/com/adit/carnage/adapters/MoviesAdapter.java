package com.adit.carnage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adit.carnage.R;
import com.adit.carnage.apis.ApiClient;
import com.adit.carnage.apis.classes.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    ApiClient client;
    private List<Movie> movieList;
    private Context context;

    public MoviesAdapter(List<Movie> list, Context ctx){
        this.movieList = list;
        this.context = ctx;
    }

    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        MovieHolder viewHolder = new MovieHolder(v);
        //Toast.makeText(context, "View Holder onCreate", Toast.LENGTH_SHORT).show();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        //Toast.makeText(context, "View Holder onBind", Toast.LENGTH_SHORT).show();

        Movie item = movieList.get(position);

        Picasso.get()
                .load(client.IMAGE_BASE_URL + item.getPoster_path().toString())
                //.resize(50, 50)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText("Judul Film : " + item.getTitle());
        holder.tvReleaseDate.setText("Tanggal Rilis : " + item.getRelease_date());
        holder.tvOverview.setText("Overview : " + item.getOverview().substring(0, 20) + "...");
        holder.tvLanguage.setText("Bahasa : " + item.getOriginal_language());
        holder.tvVoteCount.setText("Likes : " + item.getVoteCount());
    }

    @Override
    public int getItemCount() {
        //Toast.makeText(context, "getItemCount : " + popularMovieList.size(), Toast.LENGTH_SHORT).show();
        return movieList.size();
    }

    public static class MovieHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivMoviePoster)
        ImageView ivMoviePoster;

        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;

        @BindView(R.id.tvReleaseDate)
        TextView tvReleaseDate;

        @BindView(R.id.tvOverview)
        TextView tvOverview;

        @BindView(R.id.tvLanguage)
        TextView tvLanguage;

        @BindView(R.id.tvVoteCount)
        TextView tvVoteCount;

        public MovieHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
