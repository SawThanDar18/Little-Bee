package com.busybees.little_bee.adapters.Orders;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.HistoryDetailActivity;
import com.busybees.little_bee.data.vos.MyOrders.ProductPriceVO;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class MyHistoryDetailAdapter extends RecyclerView.Adapter<MyHistoryDetailAdapter.MyViewHolder> {

    Context context;
    List<ProductPriceVO> list;

    public MyHistoryDetailAdapter(HistoryDetailActivity historyDetailActivity, List<ProductPriceVO> product) {
        this.context=historyDetailActivity;
        this.list=product;
    }

    @Override
    public MyHistoryDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_detail_itemview, parent, false);
        MyHistoryDetailAdapter.MyViewHolder holder = new MyHistoryDetailAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHistoryDetailAdapter.MyViewHolder holder, int position) {

        ProductPriceVO obj = list.get(position);

        if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")){
            if ( MDetect.INSTANCE.isUnicode()){
                holder.serviceName.setText("." + obj.getServiceNameMm());
            } else  {
                holder.serviceName.setText("." + obj.getServiceNameMm());
            }
        } else if (checkLng(context).equalsIgnoreCase("zh")) {
          holder.serviceName.setText("." + obj.getServiceNameCh());
        } else{
            holder.serviceName.setText(obj.getServiceName());
        }

        if (obj.getTotalDiscount() != 0){
            holder.qty.setText(String.valueOf(obj.getQuantity()));
            holder.disc.setText(NumberFormat.getNumberInstance(Locale.US).format(obj.getTotalDiscount()));
            holder.price.setText(String.valueOf(Html.fromHtml(NumberFormat.getNumberInstance(Locale.US).format(obj.getPrice()) +"<span style=\"color:#b57c2b;\">")));
        }else {
            holder.disc.setText("-");
            holder.qty.setText(String.valueOf(obj.getQuantity()));
            holder.price.setText(String.valueOf(Html.fromHtml(NumberFormat.getNumberInstance(Locale.US).format(obj.getPrice()) +"<span style=\"color:#b57c2b;\">")));
        }

        if (obj.getTotalPrice() != 0) {
            holder.price.setText(String.valueOf(Html.fromHtml(NumberFormat.getNumberInstance(Locale.US).format(obj.getPrice()) +"<span style=\"color:#b57c2b;\">")));
        } else {
            holder.price.setText("-");
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView serviceName,price,qty,disc;

        public MyViewHolder(View view) {
            super(view);

            serviceName =view.findViewById(R.id.order_service_name);
            price = view.findViewById(R.id.order_price);
            qty=view.findViewById(R.id.order_qty);
            disc=view.findViewById(R.id.order_discount);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}


