package com.busybees.lauk_kaing_expert_services.adapters.Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    public ProductActivity click;

    public ProductAdapter(ProductActivity productActivity) {
        this.context = productActivity;
    }

    public void setClick(ProductActivity productActivity) {
        this.click=productActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private ImageView productIcon;
        private CardView productCardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productIcon = itemView.findViewById(R.id.product_icon);
            productCardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.product_itemview_adapter, parent, false);
        return new ProductAdapter.MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));
        Glide.with(context)
                .load(R.drawable.icon_cleaning__1200_x_1200_px_)
                .apply(requestOptions)
                .into(holder.productIcon);

        holder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.intentToSubProductActivity();
            }
        });

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
        return 4;
    }

}


