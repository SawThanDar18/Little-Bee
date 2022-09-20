package com.busybees.little_bee.adapters.AddMoreServices;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.little_bee.BottomSheetDialog.AddMoreServicesDialog;
import com.busybees.little_bee.R;
import com.busybees.little_bee.data.vos.Home.ServiceAvailableVO;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import java.util.ArrayList;

import me.myatminsoe.mdetect.MDetect;

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

        Glide.with(holder.itemView.getContext())
                .load(serviceAvailableVO.getServiceImage())
                .apply(requestOptions)
                .into(holder.imageView);

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it") || checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            if (MDetect.INSTANCE.isUnicode()) {

                Utility.addFontSuHome(holder.serviceName, serviceAvailableVO.getNameMm());

            } else {

                Utility.changeFontUni2ZgHome(holder.serviceName, serviceAvailableVO.getNameMm());
            }
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
            holder.serviceName.setTextColor(Color.parseColor("#000000"));

        }else {
            holder.serviceName.setTextColor(Color.parseColor("#C6C2C2"));

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


