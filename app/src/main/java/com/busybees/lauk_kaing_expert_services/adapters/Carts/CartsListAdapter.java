package com.busybees.lauk_kaing_expert_services.adapters.Carts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CartsListAdapter extends RecyclerView.Adapter<CartsListAdapter.MyViewHolder> implements View.OnClickListener {

    private View.OnClickListener onClickListener;
    Context mContext;

    public CartsListAdapter(FragmentActivity activity) {
        this.mContext=activity;
    }

    @Override
    public void onClick(View v) {
        if(v != null && v.getTag() != null && onClickListener!= null) {
            onClickListener.onClick(v);
        }
    }

    public void setCLick(View.OnClickListener onClickListener) {
        this.onClickListener=onClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public CartsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.cart_itemview_adapter, parent, false);
        return new CartsListAdapter.MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final CartsListAdapter.MyViewHolder holder, int position) {


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
        return 3;
    }

}
