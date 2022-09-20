package com.busybees.little_bee.adapters.SaveOrderReview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.RatingActivity;
import com.busybees.little_bee.data.vos.MyOrders.VendorInfoVO;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;

import java.util.List;

public class VendorRatingAdapter extends RecyclerView.Adapter<VendorRatingAdapter.MyViewHolder> {

    Context context;
    List<VendorInfoVO> list;
    RatingActivity click;
    VendorInfoVO obj;

    public VendorRatingAdapter(RatingActivity ratingActivity, List<VendorInfoVO> vendorData) {
        this.context=ratingActivity;
        this.list=vendorData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_rate_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        obj = list.get(position);
        holder.vname.setText("( "+ obj.getVendorName()+" )");

        holder.rate.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> click.RateClick(obj,ratingBar.getRating()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClick(RatingActivity ratingActivity) {
        this.click=ratingActivity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView vname;
        private RatingBar rate;

        public MyViewHolder(View view) {
            super(view);


            vname=view.findViewById(R.id.vendor_name);
            rate=view.findViewById(R.id.vendor_rating);

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
