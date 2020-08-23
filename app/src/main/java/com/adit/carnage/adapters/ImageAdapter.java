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
import com.adit.carnage.classes.Pictures;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private List<Pictures> picturesList;
    private Context context;

    public ImageAdapter(){

    }

    public ImageAdapter(List<Pictures> pictures){
        this.picturesList = pictures;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_holder, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Pictures item = picturesList.get(position);

        Picasso.get().load(item.getUri())
                .placeholder(R.drawable.carnage_logo)
                .resize(250,250)
                .into(holder.imgThumb);

        holder.tvImageName.setText("ImgName : " + item.getName());
        holder.tvImageSize.setText("ImgSize : " + item.getSize());
        holder.tvImageUri.setText("ImgUri : " + item.getUri().getEncodedPath());
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imgThumb)
        ImageView imgThumb;

        @BindView(R.id.tvImageName)
        TextView tvImageName;

        @BindView(R.id.tvImageSize)
        TextView tvImageSize;

        @BindView(R.id.tvImageUri)
        TextView tvImageUri;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
