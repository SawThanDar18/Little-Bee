package com.busybees.lauk_kaing_expert_services.adapters.Home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.data.vos.Home.PopularServicesVO;
import com.busybees.data.vos.Home.ServiceNeedVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SymnAdapter extends RecyclerView.Adapter<SymnAdapter.MyViewHolder> {

    Context context;
    List<ServiceNeedVO> serviceNeedVOList;

    public SymnAdapter(FragmentActivity activity, List<ServiceNeedVO> datas) {
        this.context=activity;
        this.serviceNeedVOList = datas;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView serviceName, price;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            serviceName = itemView.findViewById(R.id.serviceName);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.symn_adapter_itemview, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ServiceNeedVO serviceNeedVO = serviceNeedVOList.get(position);

        holder.serviceName.setText(serviceNeedVO.getName());

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it")){
            Utility.addFontSuHome(holder.serviceName, serviceNeedVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            Utility.changeFontZg2UniHome(holder.serviceName, serviceNeedVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
            holder.serviceName.setText(serviceNeedVO.getNameCh());
        } else {
            holder.serviceName.setText(serviceNeedVO.getName());
        }

        if(Double.parseDouble(String.valueOf(serviceNeedVO.getOriginalPrice())) == 0){
            holder.price.setVisibility(View.INVISIBLE);

        } else {
            holder.price.setVisibility(View.VISIBLE);
            holder.price.setText(NumberFormat.getNumberInstance(Locale.US).format(serviceNeedVO.getOriginalPrice()) + " " + context.getString(R.string.currency));

        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);

        Glide.with(holder.itemView.getContext())
                .load(serviceNeedVO.getServiceNeedsImage())
                .apply(requestOptions)
                .into(holder.imageView);
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
        return serviceNeedVOList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}

