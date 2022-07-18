package com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.data.vos.Home.ServiceAvailableVO;
import com.busybees.lauk_kaing_expert_services.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.ArrayList;
import java.util.List;

public class ServicesTitleAndIconAdapter extends RecyclerView.Adapter<ServicesTitleAndIconAdapter.MyViewHolder> {

    Context context;
    ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList = new ArrayList<ServiceAvailableVO>();
    public AddMoreServicesDialog click;
    int row_index;

    public ServicesTitleAndIconAdapter(FragmentActivity activity, ArrayList<ServiceAvailableVO> serviceAvailableVOArrayList) {
        this.context = activity;
        this.serviceAvailableVOArrayList = serviceAvailableVOArrayList;
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

        ServiceAvailableVO serviceAvailableVO = serviceAvailableVOArrayList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context.getApplicationContext(), 8)));

        GlideToVectorYou.init()
                .with(context.getApplicationContext())
                .setPlaceHolder(R.drawable.loader_circle_shape, R.drawable.loader_circle_shape)
                .load(Uri.parse(serviceAvailableVO.getServiceImage()), holder.imageView);

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it")){
            Utility.addFontSuHome(holder.serviceName, serviceAvailableVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            Utility.changeFontZg2UniHome(holder.serviceName, serviceAvailableVO.getNameMm());
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
            holder.serviceName.setText(serviceAvailableVO.getNameCh());
        } else {
            holder.serviceName.setText(serviceAvailableVO.getName());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                click.ShowProduct(serviceAvailableVO);
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
        this.click = addMoreServicesDialog;
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
        return serviceAvailableVOArrayList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}


