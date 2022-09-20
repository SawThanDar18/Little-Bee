package com.busybees.little_bee.adapters.Products;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.data.vos.ServiceDetail.ProductPriceVO;
import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.ServiceDetailActivity;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.URLImageParser;
import com.busybees.little_bee.utility.Utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.MyViewHolder> implements View.OnClickListener {

    private View.OnClickListener onClickListener;
    Context mContext;
    List<ProductPriceVO> productPriceVOList;

    public ServiceDetailAdapter(ServiceDetailActivity serviceDetailActivity, ArrayList<ProductPriceVO> datas) {
        this.mContext = serviceDetailActivity;
        this.productPriceVOList = datas;
    }


    @Override
    public void onClick(View v) {
        if(v != null && v.getTag(R.id.position) != null && onClickListener!= null) {
            onClickListener.onClick(v);
        }
    }

    public void setClick(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceDetailName, selectServiceText, cancelServiceText, surveyRequestText, originalPrice, discountPrice, savePricePercent, serviceDetailDescription, hideShowDetailText, promoAvailable;
        private LinearLayout addLayoutMainView;

        public MyViewHolder(View itemView) {
            super(itemView);

            serviceDetailName = itemView.findViewById(R.id.service_detail_name);
            selectServiceText = itemView.findViewById(R.id.selectText);
            cancelServiceText = itemView.findViewById(R.id.cancel);
            surveyRequestText = itemView.findViewById(R.id.survey_requested_text);
            originalPrice = itemView.findViewById(R.id.origin_price);
            discountPrice = itemView.findViewById(R.id.discount_price);
            savePricePercent = itemView.findViewById(R.id.save);
            serviceDetailDescription = itemView.findViewById(R.id.serviceDetail);
            hideShowDetailText = itemView.findViewById(R.id.hideShowDetail);
            addLayoutMainView = itemView.findViewById(R.id.addLayoutMainView);
            promoAvailable = itemView.findViewById(R.id.promo_available);
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

        if (holder != null && productPriceVO != null) {
            if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it") || checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
                if (MDetect.INSTANCE.isUnicode()) {

                    Utility.addFontSuHome(holder.serviceDetailName, productPriceVO.getNameMm());

                } else {

                    Utility.changeFontUni2ZgHome(holder.serviceDetailName, productPriceVO.getNameMm());
                }
            } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
                holder.serviceDetailName.setText(productPriceVO.getNameCh());
            } else {
                holder.serviceDetailName.setText(productPriceVO.getName());
            }

            if (productPriceVO.getPromoId() != null) {
                holder.promoAvailable.setVisibility(View.VISIBLE);
            } else {
                holder.promoAvailable.setVisibility(View.GONE);
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

                    holder.originalPrice.setText(mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getOriginalPrice()));
                } else {
                    holder.discountPrice.setVisibility(View.VISIBLE);
                    holder.savePricePercent.setVisibility(View.VISIBLE);
                    holder.originalPrice.setVisibility(View.VISIBLE);

                    holder.originalPrice.setText(mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getOriginalPrice()));
                    holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.discountPrice.setText( mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(productPriceVO.getDiscountPrice()));
                    holder.savePricePercent.setText(mContext.getString(R.string.save) + " " + getSavePercentage(productPriceVO.getOriginalPrice(), productPriceVO.getDiscountPrice()));
                }
            }

            /*String quantity = productPriceVO.getQuantity() != null ? String.valueOf(productPriceVO.getQuantity()) : "0";
            holder.selectServiceText.setVisibility(View.VISIBLE);
            holder.cancelServiceText.setVisibility(View.GONE);
            holder.surveyRequestText.setVisibility(View.GONE);
            if(Integer.parseInt(quantity) == 0) {

                holder.selectServiceText.setVisibility(View.VISIBLE);
                holder.surveyRequestText.setVisibility(View.GONE);

                if(Double.parseDouble(String.valueOf(productPriceVO.getFormStatus()))==2) {
                    if (checkLng(mContext).equalsIgnoreCase("it")) {

                        if(holder.serviceDetailName.getText().length()>35){
                            holder.serviceDetailName.setEms(11);
                            holder.serviceDetailName.setLines(2);

                        }

                    }
                    holder.selectServiceText.setText(mContext.getString(R.string.survey));
                }else{

                    holder.selectServiceText.setText(mContext.getString(R.string.selected));
                }

                holder.selectServiceText.setText(mContext.getString(R.string.selected));
                holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_white_color));
                holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));


                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);


            } else  {
                if(Double.parseDouble(String.valueOf(productPriceVO.getFormStatus()))==2) {
                    holder.surveyRequestText.setVisibility(View.VISIBLE);
                    holder.surveyRequestText.setText(mContext.getResources().getString(R.string.survey_request));
                    holder.surveyRequestText.setPadding(mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10));

                    holder.selectServiceText.setVisibility(View.GONE);
                    holder.cancelServiceText.setVisibility(View.VISIBLE);

                    holder.cancelServiceText.setTag(R.id.number,quantity);
                    holder.cancelServiceText.setTag(R.id.position,position);
                    holder.cancelServiceText.setOnClickListener(this);
                }else{
                    holder.surveyRequestText.setVisibility(View.GONE);
                    holder.selectServiceText.setVisibility(View.VISIBLE);

                    holder.selectServiceText.setText(mContext.getString(R.string.unselected));
                    holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_theme_bg));
                    holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                }

                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);
            }*/


            Log.e("adapter>>>>", String.valueOf(productPriceVO.getQuantity()));
            int quantity = productPriceVO.getQuantity() != null ? productPriceVO.getQuantity() : 0;

            holder.selectServiceText.setVisibility(View.VISIBLE);
            holder.cancelServiceText.setVisibility(View.GONE);
            holder.surveyRequestText.setVisibility(View.GONE);
            if(quantity == 0) {

                holder.selectServiceText.setVisibility(View.VISIBLE);
                holder.surveyRequestText.setVisibility(View.GONE);

                if(productPriceVO.getFormStatus() == 2) {
                    if (checkLng(mContext).equalsIgnoreCase("it")) {

                        if(holder.serviceDetailName.getText().length()>35){
                            holder.serviceDetailName.setEms(11);
                            holder.serviceDetailName.setLines(2);
                        }

                    }

                    holder.selectServiceText.setText(mContext.getString(R.string.survey));
                    holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_black_color));
                    holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                }else if (productPriceVO.getFormStatus() == 0){

                    holder.selectServiceText.setText(mContext.getString(R.string.selected));
                } else {
                    if (productPriceVO.getOriginalPrice() == 0) {
                        if (checkLng(mContext).equalsIgnoreCase("it")) {

                            if(holder.serviceDetailName.getText().length()>35){
                                holder.serviceDetailName.setEms(11);
                                holder.serviceDetailName.setLines(2);
                            }

                        }

                        holder.selectServiceText.setText(mContext.getString(R.string.survey));
                    } else {
                        holder.selectServiceText.setText(mContext.getString(R.string.selected));
                    }
                    holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_black_color));
                    holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                }

                /*holder.selectServiceText.setText(mContext.getString(R.string.selected));
                holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_white_color));
                holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));*/
                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);


            } else  {

                if(productPriceVO.getFormStatus() == 2) {

                    holder.surveyRequestText.setVisibility(View.VISIBLE);
                    holder.surveyRequestText.setText(mContext.getResources().getString(R.string.survey_request));
                    holder.surveyRequestText.setPadding(mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10));

                    holder.selectServiceText.setVisibility(View.GONE);
                    holder.cancelServiceText.setVisibility(View.VISIBLE);
                    holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.two_color_shape));
                    holder.cancelServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                    holder.cancelServiceText.setTag(R.id.number,quantity);
                    holder.cancelServiceText.setTag(R.id.position,position);
                    holder.cancelServiceText.setOnClickListener(this);

                }else if (productPriceVO.getFormStatus() == 0){
                    holder.surveyRequestText.setVisibility(View.GONE);
                    holder.selectServiceText.setVisibility(View.VISIBLE);

                    holder.selectServiceText.setText(mContext.getString(R.string.unselected));
                    holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.two_color_shape));
                    holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                } else {
                    if (productPriceVO.getOriginalPrice() == 0) {

                        if (productPriceVO.getDescription() == null) {
                            holder.surveyRequestText.setVisibility(View.VISIBLE);
                            holder.surveyRequestText.setText(mContext.getResources().getString(R.string.survey_request));
                            holder.surveyRequestText.setPadding(mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                                    mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                                    mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                                    mContext.getResources().getDimensionPixelOffset(R.dimen.margin10));
                        } else {
                            holder.surveyRequestText.setVisibility(View.GONE);
                        }

                        holder.selectServiceText.setVisibility(View.GONE);
                        holder.cancelServiceText.setVisibility(View.VISIBLE);
                        holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.two_color_shape));
                        holder.cancelServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                        holder.cancelServiceText.setTag(R.id.number,quantity);
                        holder.cancelServiceText.setTag(R.id.position,position);
                        holder.cancelServiceText.setOnClickListener(this);
                    } else {
                        holder.surveyRequestText.setVisibility(View.GONE);
                        holder.selectServiceText.setVisibility(View.VISIBLE);

                        holder.selectServiceText.setText(mContext.getString(R.string.unselected));
                        holder.addLayoutMainView.setBackground(mContext.getResources().getDrawable(R.drawable.two_color_shape));
                        holder.selectServiceText.setTextColor(mContext.getResources().getColor(R.color.black));

                    }
                }


                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);

            }


            holder.hideShowDetailText.setVisibility(View.VISIBLE);

            if(productPriceVO.isShowDetail() == true && productPriceVO.getDescription() != null) {

                holder.serviceDetailDescription.setVisibility(View.VISIBLE);

                if (productPriceVO.getFormStatus() == 2) {
                    holder.serviceDetailDescription.setVisibility(View.GONE);
                }

                if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it") || checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
                    if (MDetect.INSTANCE.isUnicode()) {

                        Utility.addFontSuHome(holder.serviceDetailName, productPriceVO.getNameMm());

                    } else {

                        Utility.changeFontUni2ZgHome(holder.serviceDetailName, productPriceVO.getNameMm());
                    }
                } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
                    holder.serviceDetailName.setText(productPriceVO.getNameCh());
                } else {
                    holder.serviceDetailName.setText(productPriceVO.getName());
                }

                if (checkLng(mContext).equalsIgnoreCase("it") || checkLng(mContext).equalsIgnoreCase("fr")) {
                    String nameMM = productPriceVO.getDescriptionMm();
                    if ( MDetect.INSTANCE.isUnicode()){
                        nameMM = productPriceVO.getDescriptionMm();

                    }
                    else  {
                        nameMM=Utility.changeFontUni2ZgString(nameMM);
                    }

                    URLImageParser p = new URLImageParser( holder.serviceDetailDescription, mContext);
                    Spanned htmlSpan = Html.fromHtml(nameMM, p, new Utility.UlTagHandler());
                    holder.serviceDetailDescription.setText (htmlSpan);

                } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
                    holder.serviceDetailDescription.setText(productPriceVO.getDescriptionCh());
                    URLImageParser p = new URLImageParser( holder.serviceDetailDescription, mContext);
                    Spanned htmlSpan = Html.fromHtml(productPriceVO.getDescriptionCh(), p, new Utility.UlTagHandler());
                    holder.serviceDetailDescription.setText (htmlSpan);
                } else {
                    holder.serviceDetailDescription.setText(productPriceVO.getDescription());
                    URLImageParser p = new URLImageParser( holder.serviceDetailDescription, mContext);
                    Spanned htmlSpan = Html.fromHtml(productPriceVO.getDescription(), p, new Utility.UlTagHandler());
                    holder.serviceDetailDescription.setText (htmlSpan);
                }

                holder.hideShowDetailText.setVisibility(View.VISIBLE);
                holder.hideShowDetailText.setText(mContext.getString(R.string.hide_detail));
                holder.hideShowDetailText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_up_black,0);

            }else if (productPriceVO.getDescription() == null){
                holder.hideShowDetailText.setVisibility(View.GONE);

            }else{
                holder.hideShowDetailText.setVisibility(View.VISIBLE);
                holder.hideShowDetailText.setText(mContext.getString(R.string.show_detail));
                holder.hideShowDetailText.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop_down_black,0);
            }

            holder.hideShowDetailText.setTag(R.id.position,position);
            holder.hideShowDetailText.setOnClickListener(this);
            //holder.addBtnLayout.setPadding(20,0,20,0);
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