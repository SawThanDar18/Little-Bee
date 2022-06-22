package com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.lauk_kaing_expert_services.R;

import java.util.List;

public class ServicesTitleAndIconAdapter extends RecyclerView.Adapter<ServicesTitleAndIconAdapter.MyViewHolder> {

    Context context;
    private List<Integer> urlList;
    private List<String> serviceName;
    public AddMoreServicesDialog click;
    int row_index;

    public ServicesTitleAndIconAdapter(FragmentActivity activity, List<Integer> urlList, List<String> serviceName) {
        this.context=activity;
        this.urlList = urlList;
        this.serviceName = serviceName;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView serviceName;
        private LinearLayout cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.serviceIcon);
            serviceName = itemView.findViewById(R.id.service_name);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public ServicesTitleAndIconAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.services_title_and_icon_item_view_adapter, parent, false);
        return new ServicesTitleAndIconAdapter.MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();
        Integer url = urlList.get(P);
        ImageView img = holder.imageView;

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_about_jia_jie);
        requestOptions.error(R.drawable.ic_about_jia_jie);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(img);

        String names = serviceName.get(P);
        holder.serviceName.setText(names);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                click.ShowProduct();
                notifyDataSetChanged();
            }
        });

        if(row_index==position) {
            holder.serviceName.setTextColor(Color.parseColor("#ffb4b6"));

        }else {
            holder.serviceName.setTextColor(Color.parseColor("#000000"));

        }
    }

    public void setClick(AddMoreServicesDialog addMoreServicesDialog) {
        this.click=addMoreServicesDialog;
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
        return 12;
    }
}


