package com.busybees.lauk_kaing_expert_services.adapters.Products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.SubProductActivity;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class SubProductAdapter extends RecyclerView.Adapter<SubProductAdapter.MyViewHolder> {

    Context context;
    public SubProductActivity click;


    public SubProductAdapter(SubProductActivity subProductActivity) {
        this.context = subProductActivity;
    }

    public void setClick(SubProductActivity subProductActivity) {
        this.click = subProductActivity;
    }

    public void setProductClick(SubProductActivity subProductActivity, int position) {
        this.click = subProductActivity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView subProductName;
        private Button selectBtn;
        private ImageView subProductImage;

        public MyViewHolder(View itemView) {
            super(itemView);


            subProductName = itemView.findViewById(R.id.sub_product_name);
            subProductImage = itemView.findViewById(R.id.sub_product_image);
            selectBtn = itemView.findViewById(R.id.select_btn);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.subproduct_item_view_adapter, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_bw);
        requestOptions.error(R.drawable.logo_bw);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 22)));

        Glide.with(context)
                .load("https://busybeesexpertservice.com/upload/product_images_new/CS_cleaning_upholstery_sofa.png")
                .apply(requestOptions)
                .into(holder.subProductImage);
        setBannerImageSize(holder.subProductImage);

    }

    private void setBannerImageSize(View view) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        view.getLayoutParams().height = (int) (display.getHeight() / 4);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

}

