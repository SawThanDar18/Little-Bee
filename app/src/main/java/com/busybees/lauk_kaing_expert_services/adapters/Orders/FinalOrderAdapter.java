package com.busybees.lauk_kaing_expert_services.adapters.Orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.FinalOrderActivity;

public class FinalOrderAdapter extends RecyclerView.Adapter<FinalOrderAdapter.MyViewHolder> implements View.OnClickListener {

    private View.OnClickListener onClickListener;
    Context mContext;

    public FinalOrderAdapter(FinalOrderActivity activity) {
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
    public FinalOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.final_order_itemview_adapter, parent, false);
        return new FinalOrderAdapter.MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final FinalOrderAdapter.MyViewHolder holder, int position) {


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
