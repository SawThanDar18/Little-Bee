package com.busybees.lauk_kaing_expert_services.adapters.Receipt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.HistoryDetailActivity;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.VendorInfoVO;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.List;

public class HistoryVendorInfoAdapter extends RecyclerView.Adapter<HistoryVendorInfoAdapter.MyViewHolder> {

    Context context;
    List<VendorInfoVO> list;

    public HistoryVendorInfoAdapter(HistoryDetailActivity historyDetailActivity, List<VendorInfoVO> vendorData) {
        this.context=historyDetailActivity;
        this.list=vendorData;
    }


    @Override
    public HistoryVendorInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_info_adapter, parent, false);
        HistoryVendorInfoAdapter.MyViewHolder holder = new HistoryVendorInfoAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryVendorInfoAdapter.MyViewHolder holder, int position) {

        VendorInfoVO obj = list.get(position);

        holder.name.setText("NAME : "+obj.getVendorName());
        holder.phone.setText("PHONE : "+obj.getVendorPhone());
        holder.code.setText("CODE : "+obj.getVendorCode());
        holder.phone.setVisibility(View.GONE);
        holder.call_now.setVisibility(View.GONE);

        holder.copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", obj.getVendorCode());
            clipboard.setPrimaryClip(clip);
            Utility.showToast(context, "Copied");
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView serviceName,name,phone,code;
        private Button call_now;
        private ImageView copy;

        public MyViewHolder(View view) {
            super(view);

            serviceName =view.findViewById(R.id.serviceName);
            name = view.findViewById(R.id.name);
            phone=view.findViewById(R.id.phone);
            code=view.findViewById(R.id.code);
            call_now=view.findViewById(R.id.btn_call);
            copy = view.findViewById(R.id.copy);

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
