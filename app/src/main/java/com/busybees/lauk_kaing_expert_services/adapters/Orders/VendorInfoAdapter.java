package com.busybees.lauk_kaing_expert_services.adapters.Orders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.Dialog.DialogCallVendor;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.OrderDetailActivity;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.VendorInfoVO;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.List;

public class VendorInfoAdapter extends RecyclerView.Adapter<VendorInfoAdapter.MyViewHolder> {

    Context context;
    List<VendorInfoVO> list;
    String status;

    public VendorInfoAdapter(OrderDetailActivity orderDetailActivity, List<VendorInfoVO> vendorData, String status) {
        this.context=orderDetailActivity;
        this.list=vendorData;
        this.status=status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_info_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VendorInfoVO obj = list.get(position);

        holder.name.setText("NAME : "+obj.getVendorName());
        holder.phone.setText("PHONE : "+obj.getVendorPhone());

        if (status.equalsIgnoreCase("Confirmed") || status.equalsIgnoreCase("Upcoming")){
            holder.phone.setVisibility(View.GONE);
            holder.call_now.setVisibility(View.GONE);
        }else {
            holder.phone.setVisibility(View.VISIBLE);
            holder.call_now.setVisibility(View.VISIBLE);
        }

        holder.call_now.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity)(context);
            FragmentManager fm = activity.getSupportFragmentManager();
            DialogCallVendor dialogVendorCall=new DialogCallVendor(obj);
            dialogVendorCall.show(fm,"");
        });

        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", obj.getVendorCode());
                clipboard.setPrimaryClip(clip);
                Utility.showToast(context, "Copied");
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView serviceName,name,phone,code;
        private ImageView call_now;
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
