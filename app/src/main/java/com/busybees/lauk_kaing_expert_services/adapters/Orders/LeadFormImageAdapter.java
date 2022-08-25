package com.busybees.lauk_kaing_expert_services.adapters.Orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.FullImageActivity;
import com.busybees.lauk_kaing_expert_services.activity.ImageViewActivity;

import java.io.IOException;
import java.util.List;

public class LeadFormImageAdapter extends RecyclerView.Adapter<LeadFormImageAdapter.LeadFormImageViewHolder> {

    private Context context;
    private List<String> image_URL;

    public LeadFormImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> image_URL) {
        this.image_URL = image_URL;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LeadFormImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_itemview, parent, false);
        return new LeadFormImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadFormImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String image = image_URL.get(position);
        Glide.with(context).load(image).into(holder.item_imageView);

        holder.item_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("image", image_URL.get(position));
                context.startActivity(intent);
                //context.startActivity(new Intent(context, FullImageActivity.class).putExtra("image", image_URL.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (image_URL != null) {
            return image_URL.size();
        }
        return 0;
    }

    public class LeadFormImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_imageView;

        public LeadFormImageViewHolder(@NonNull View itemView) {
            super(itemView);

            item_imageView = itemView.findViewById(R.id.item_imageView);
        }
    }
}
