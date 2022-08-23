package com.busybees.lauk_kaing_expert_services.adapters.Payment;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.PaymentActivity;
import com.busybees.lauk_kaing_expert_services.data.models.GetCart.GetCartDataModel;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    Context context;
    List<GetCartDataModel> list;

    public PaymentAdapter(PaymentActivity paymentActivity, ArrayList<GetCartDataModel> cartDatas) {
        this.context=paymentActivity;
        this.list = cartDatas;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView rec_no_f, rec_service_name, rec_qty, rec_discount, price, rec_amt;

        public MyViewHolder(View itemView) {
            super(itemView);

            rec_no_f = (TextView) itemView.findViewById(R.id.rec_no_f);
            rec_service_name = (TextView) itemView.findViewById(R.id.rec_dec);
            price = (TextView) itemView.findViewById(R.id.rec_price);
            rec_qty = (TextView) itemView.findViewById(R.id.rec_unit);
            rec_discount = (TextView) itemView.findViewById(R.id.rec_discount);
            rec_amt = (TextView) itemView.findViewById(R.id.rec_amt);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.payment_receipt_item_view, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetCartDataModel obj = list.get(position);

        int rec_no = position+1;

        if(rec_no>0) {
            holder.rec_no_f.setText(String.valueOf(rec_no));
        }
        if (obj.getDiscount() == 0){
            holder.rec_discount.setText("-");
        }else if (obj.getDiscount() > 0){
            holder.rec_discount.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(obj.getDiscount())));
        }

        if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")){

            if(obj.getOriginalPrice() == 0){

                holder.rec_service_name.setText(Html.fromHtml(obj.getNameMm()+" "+"<span style='color:red !important;'>(Survey)</span>"));

            }else{
                holder.rec_service_name.setText(obj.getNameMm());
            }
        }else{
            if(obj.getOriginalPrice() == 0){
                holder.rec_service_name.setText(Html.fromHtml(obj.getName()+" "+"<span style='color:red !important;'>(Survey)</span>"));
            }else{
                holder.rec_service_name.setText(obj.getName());
            }
        }
        holder.rec_qty.setText(String.valueOf(obj.getQuantity()));

        if (obj.getAmount() == 0) {
            holder.rec_amt.setText("-");
        } else {
            holder.rec_amt.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(obj.getAmount())));
        }

        if (obj.getOriginalPrice() == 0) {
            holder.price.setText("-");
        } else {
            holder.price.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(obj.getOriginalPrice())));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

}

