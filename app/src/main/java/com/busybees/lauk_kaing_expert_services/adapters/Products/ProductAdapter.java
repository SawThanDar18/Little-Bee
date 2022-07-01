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
import com.busybees.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ProductActivity;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<ProductsVO> productsVOList;
    public ProductActivity click;

    public ProductAdapter(ProductActivity productActivity, ArrayList<ProductsVO> datas) {
        this.context = productActivity;
        this.productsVOList = datas;
    }

    public void setClick(ProductActivity productActivity) {
        this.click = productActivity;
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

        ProductsVO productsVO = productsVOList.get(position);

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it")){
            Utility.addFontSuHome(holder.productName, productsVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            Utility.changeFontZg2UniHome(holder.productName, productsVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
            holder.productName.setText(productsVO.getNameCh());
        } else {
            holder.productName.setText(productsVO.getName());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));
        Glide.with(context)
                .load(productsVO.getProductImage())
                .apply(requestOptions)
                .into(holder.productIcon);

        holder.productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.intentToSubProductActivity(productsVO);
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
        return productsVOList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

}


