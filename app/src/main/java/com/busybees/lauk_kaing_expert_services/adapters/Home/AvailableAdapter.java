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

import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.List;

public class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.MyViewHolder> {

    Context context;
    List<ServiceAvailableVO> serviceAvailableVOList;

    public AvailableAdapter(FragmentActivity activity, List<ServiceAvailableVO> datas) {
        this.context=activity;
        this.serviceAvailableVOList = datas;
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

        ServiceAvailableVO serviceAvailableVO = serviceAvailableVOList.get(position);

        holder.serviceName.setText(serviceAvailableVO.getName());

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it")){
            Utility.addFontSuHome(holder.serviceName, serviceAvailableVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            Utility.changeFontZg2UniHome(holder.serviceName, serviceAvailableVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
            holder.serviceName.setText(serviceAvailableVO.getNameCh());
        } else {
            holder.serviceName.setText(serviceAvailableVO.getName());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);

        GlideToVectorYou.init()
                .with(context.getApplicationContext())
                .setPlaceHolder(R.drawable.loader_circle_shape, R.drawable.loader_circle_shape)
                .load(Uri.parse(serviceAvailableVO.getServiceImage()), holder.imageView);

        /*Glide.with(holder.itemView.getContext())
                .load(serviceAvailableVO.getServiceImage())
                .apply(requestOptions)
                .into(holder.imageView);*/
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
        return serviceAvailableVOList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }


}

