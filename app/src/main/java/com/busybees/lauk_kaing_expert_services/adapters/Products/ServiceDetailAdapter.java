package com.busybees.lauk_kaing_expert_services.adapters.Products;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busybees.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.ServiceDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.MyViewHolder> implements View.OnClickListener {

    private View.OnClickListener onClickListener;
    Context mContext;
    List<ProductPriceVO> productPriceVOList;

    public ServiceDetailActivity click;

    public ServiceDetailAdapter(ServiceDetailActivity serviceDetailActivity, ArrayList<ProductPriceVO> datas) {
        this.mContext = serviceDetailActivity;
        this.productPriceVOList = datas;
    }


    @Override
    public void onClick(View v) {

    }

    public void setClick(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceDetailName, selectServiceText, cancelServiceText, originalPrice, discountPrice, savePricePercent, serviceDetailDescription, hideShowDetailText;

        public MyViewHolder(View itemView) {
            super(itemView);

            serviceDetailName = itemView.findViewById(R.id.service_detail_name);
            selectServiceText = itemView.findViewById(R.id.selectText);
            cancelServiceText = itemView.findViewById(R.id.cancel);
            originalPrice = itemView.findViewById(R.id.origin_price);
            discountPrice = itemView.findViewById(R.id.discount_price);
            savePricePercent = itemView.findViewById(R.id.save);
            serviceDetailDescription = itemView.findViewById(R.id.serviceDetail);
            hideShowDetailText = itemView.findViewById(R.id.hideShowDetail);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.service_detail_adapter, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductPriceVO productPriceVO = productPriceVOList.get(position);
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
        return 2;
    }

}