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
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

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

        if (productPriceVO != null) {
            if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it")){
                Utility.addFontSuHome(holder.serviceDetailName, productPriceVO.getNameMm());
            } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
                Utility.changeFontZg2UniHome(holder.serviceDetailName, productPriceVO.getNameMm());
            } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
                holder.serviceDetailName.setText(productPriceVO.getNameCh());
            } else {
                holder.serviceDetailName.setText(productPriceVO.getName());
            }

            if(holder.serviceDetailName.getText().length() > 30){
                holder.serviceDetailName.setEms(11);
                holder.serviceDetailName.setLines(2);
            }

            if(Double.parseDouble(String.valueOf(productPriceVO.getOriginalPrice())) == 0){
                holder.originalPrice.setVisibility(View.GONE);
                holder.discountPrice.setVisibility(View.GONE);
                holder.savePricePercent.setVisibility(View.GONE);

            } else {
                if(Double.parseDouble(String.valueOf(productPriceVO.getDiscount())) == 0){
                    holder.discountPrice.setVisibility(View.GONE);
                    holder.savePricePercent.setVisibility(View.GONE);
                    holder.originalPrice.setVisibility(View.VISIBLE);

                    holder.originalPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getOriginalPrice()) + " " + mContext.getString(R.string.currency));
                } else {
                    holder.discountPrice.setVisibility(View.VISIBLE);
                    holder.savePricePercent.setVisibility(View.VISIBLE);
                    holder.originalPrice.setVisibility(View.VISIBLE);

                    holder.originalPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getOriginalPrice()) + " " + mContext.getString(R.string.currency));
                    holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.discountPrice.setText(NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getDiscountPrice()) + " " + mContext.getString(R.string.currency));
                    holder.savePricePercent.setText(mContext.getString(R.string.save) + " " + getSavePercentage(productPriceVO.getOriginalPrice(), productPriceVO.getDiscountPrice()));
                }
            }
        }
    }

    private String getSavePercentage(int originalPrice , int discountPrice) {
        double srpAmt = Double.parseDouble(String.valueOf(discountPrice));
        double mrpAmt = Double.parseDouble(String.valueOf(originalPrice));
        double dis = mrpAmt - srpAmt  ;
        if(dis > 0) {
            return (new DecimalFormat("##").format((dis * 100) / mrpAmt))+"%";
        }
        return "0 %";
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
        return productPriceVOList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

}