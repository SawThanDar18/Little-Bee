package com.busybees.lauk_kaing_expert_services.adapters.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;

import java.util.List;

public class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.MyViewHolder> {

    Context context;
    private List<Integer> urlList;
    private List<String> serviceName;

    public AvailableAdapter(FragmentActivity activity, List<Integer> urlList, List<String> serviceName) {
        this.context=activity;
        this.urlList = urlList;
        this.serviceName = serviceName;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView serviceName;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.service_img);
            serviceName = itemView.findViewById(R.id.service_name);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.available_adapter_itemview, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();
        Integer url = urlList.get(P);
        ImageView img = holder.imageView;

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.logo_bw);
        requestOptions.error(R.drawable.logo_bw);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(img);

        String names = serviceName.get(P);
        holder.serviceName.setText(names);
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

