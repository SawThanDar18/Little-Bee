package com.busybees.lauk_kaing_expert_services.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.FullImageActivity;

import java.io.IOException;
import java.util.List;

public class PhotoItemViewAdapter extends RecyclerView.Adapter<PhotoItemViewAdapter.PhotoItemViewHolder> {

    private Context context;
    private List<Uri> listPhoto;

    public PhotoItemViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Uri> list) {
        this.listPhoto = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_itemview, parent, false);
        return new PhotoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Uri uri = listPhoto.get(position);
        if (uri == null) {
            return;
        }

        holder.item_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullImageActivity.class).putExtra("image", listPhoto.get(position).toString()));
            }
        });

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            if (bitmap != null) {
                holder.item_imageView.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (listPhoto != null) {
            return listPhoto.size();
        }
        return 0;
    }

    public class PhotoItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_imageView;

        public PhotoItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_imageView = itemView.findViewById(R.id.item_imageView);
        }
    }
}
