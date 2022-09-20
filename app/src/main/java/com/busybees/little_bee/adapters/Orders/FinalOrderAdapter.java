package com.busybees.little_bee.adapters.Orders;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.FinalOrderActivity;
import com.busybees.little_bee.data.models.GetCart.GetCartDataModel;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class FinalOrderAdapter extends RecyclerView.Adapter<FinalOrderAdapter.MyViewHolder> implements View.OnClickListener {

    private View.OnClickListener onClickListener;
    Context mContext;
    List<GetCartDataModel> list;

    public FinalOrderAdapter(FinalOrderActivity activity, ArrayList<GetCartDataModel> cartDatas) {
        this.mContext=activity;
        this.list=cartDatas;
    }

    @Override
    public void onClick(View v) {
        if(v != null && v.getTag(R.id.position) != null && onClickListener!= null) {
            onClickListener.onClick(v);
        }
    }

    public void setCLick(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceDetailName, selectServiceText, cancel, surveyRequestedText, discountPrice, originalPrice, save, deleteCart;
        private ImageView decrease, increase;

        public MyViewHolder(View itemView) {
            super(itemView);

            serviceDetailName = itemView.findViewById(R.id.service_detail_name);
            selectServiceText = itemView.findViewById(R.id.addBtnLayout);
            cancel = itemView.findViewById(R.id.cancel);
            surveyRequestedText = itemView.findViewById(R.id.survey_requested_text);
            discountPrice = itemView.findViewById(R.id.discount_price);
            originalPrice = itemView.findViewById(R.id.origin_price);
            save = itemView.findViewById(R.id.save);
            deleteCart = itemView.findViewById(R.id.delete_cart);
            decrease = itemView.findViewById(R.id.decreseBtn);
            increase = itemView.findViewById(R.id.increaseBtn);
        }
    }

    @Override
    public FinalOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.final_order_itemview_adapter, parent, false);
        return new FinalOrderAdapter.MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final FinalOrderAdapter.MyViewHolder holder, int position) {
        GetCartDataModel obj = list.get(position);
        String lang = AppStorePreferences.getString(mContext, AppENUM.LANG);

        MDetect.INSTANCE.init(mContext);

        if(holder != null && obj != null) {
            if(lang != null ){
                if (lang.equalsIgnoreCase("it") || lang.equalsIgnoreCase("fr")){
                    if ( MDetect.INSTANCE.isUnicode()){
                        Utility.addFontSu( holder.serviceDetailName, obj.getNameMm());
                    } else  {
                        Utility.changeFontUni2Zg( holder.serviceDetailName, obj.getNameMm());
                    }
                }  else if (lang.equalsIgnoreCase("zh")) {
                    holder.serviceDetailName.setText(obj.getNameCh());
                }
                else{
                    holder.serviceDetailName.setText(obj.getName());
                }

            }else{
                holder.serviceDetailName.setText(obj.getName());
            }

            if(holder.serviceDetailName.getText().length()>32){
                holder.serviceDetailName.setEms(11);
                holder.serviceDetailName.setLines(2);
            }

            if( Double.parseDouble(String.valueOf(obj.getOriginalPrice())) == 0.00) {
                holder.originalPrice.setVisibility(View.GONE);
                holder.discountPrice.setVisibility(View.GONE);
                holder.save.setVisibility(View.GONE);
                holder.deleteCart.setVisibility(View.GONE);

            }else{
                if(Double.parseDouble(String.valueOf(obj.getDiscountPrice())) == 0.00){
                    holder.discountPrice.setVisibility(View.GONE);
                    holder.save.setVisibility(View.GONE);
                    holder.originalPrice.setVisibility(View.VISIBLE);
                    holder.originalPrice.setText(mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(obj.getOriginalPrice()));
                    //holder.originalPrice.setTextColor(Color.parseColor("#000000"));

                }else{
                    holder.discountPrice.setVisibility(View.VISIBLE);
                    holder.originalPrice.setVisibility(View.VISIBLE);
                    holder.save.setVisibility(View.VISIBLE);
                    holder.originalPrice.setText(mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(obj.getOriginalPrice()));
                    holder.discountPrice.setText(mContext.getString(R.string.currency) + " " + NumberFormat.getNumberInstance(Locale.US).format(obj.getDiscountPrice()));
                    holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.save.setText(mContext.getString(R.string.save) + " " + getSavePercentage(obj.getOriginalPrice(), obj.getDiscountPrice()));

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.originalPrice.getLayoutParams();
                    params.setMarginStart(10);
                    holder.originalPrice.setLayoutParams(params);


                }
                holder.deleteCart.setVisibility(View.VISIBLE);
            }

            int quantity = obj.getQuantity() !=null ? obj.getQuantity() : 0;
            holder.selectServiceText.setVisibility(View.VISIBLE);
            holder.decrease.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.GONE);
            holder.surveyRequestedText.setVisibility(View.GONE);

            if(quantity == 0) {
                holder.selectServiceText.setVisibility(View.VISIBLE);
                holder.selectServiceText.setOnClickListener(this);
                holder.surveyRequestedText.setVisibility(View.GONE);

                if(Double.parseDouble(String.valueOf(obj.getOriginalPrice()))==0.00) {

                    if (checkLng(mContext).equalsIgnoreCase("it")) {
                        holder.selectServiceText.setEms(10);
                        holder.selectServiceText.setLines(3);
                    }

                    holder.selectServiceText.setText(mContext.getString(R.string.survey));
                    holder.increase.setVisibility(View.GONE);
                    holder.decrease.setVisibility(View.GONE);
                }else{
                    holder.selectServiceText.setText(mContext.getString(R.string.add));
                    holder.increase.setVisibility(View.VISIBLE);
                }

                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);
            } else {

                if(Double.parseDouble(String.valueOf(obj.getOriginalPrice()))==0.00) {
                    holder.surveyRequestedText.setVisibility(View.VISIBLE);
                    holder.surveyRequestedText.setText(mContext.getResources().getString(R.string.survey_request));
                    holder.surveyRequestedText.setPadding(mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10),
                            mContext.getResources().getDimensionPixelOffset(R.dimen.margin10));

                    holder.selectServiceText.setVisibility(View.GONE);
                    holder.increase.setVisibility(View.GONE);
                    holder.decrease.setVisibility(View.GONE);
                    holder.cancel.setVisibility(View.VISIBLE);

                    holder.cancel.setTag(R.id.number,quantity);
                    holder.cancel.setTag(R.id.position,position);
                    holder.cancel.setOnClickListener(this);
                }else{
                    holder.surveyRequestedText.setVisibility(View.GONE);
                    holder.increase.setVisibility(View.VISIBLE);
                    holder.decrease.setVisibility(View.VISIBLE);
                    holder.selectServiceText.setVisibility(View.VISIBLE);
                }

                holder.decrease.setTag(R.id.number,quantity);
                holder.decrease.setTag(R.id.position,position);
                holder.decrease.setOnClickListener(this);

                holder.increase.setTag(R.id.number,quantity);
                holder.increase.setTag(R.id.position,position);
                holder.increase.setOnClickListener(this);

                holder.selectServiceText.setTag(R.id.number,quantity);
                holder.selectServiceText.setTag(R.id.position,position);
                holder.selectServiceText.setOnClickListener(this);
                holder.selectServiceText.setText(String.valueOf(obj.getQuantity()));
            }

            //holder.viewSelect.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_nocolor_stroke));

            holder.deleteCart.setTag(R.id.number,quantity);
            holder.deleteCart.setTag(R.id.position,position);
            holder.deleteCart.setOnClickListener(this);


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

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
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
        return list.size();
    }

}
