package com.busybees.little_bee.adapters.Home;

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
import com.busybees.little_bee.data.vos.Home.PopularServicesVO;
import com.busybees.little_bee.R;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;
import com.busybees.little_bee.utility.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.MDetect;

public class PopularAdapter  extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    Context context;
    List<PopularServicesVO> popularServicesVOList;

    public PopularAdapter(FragmentActivity activity, List<PopularServicesVO> datas) {
        this.context=activity;
        this.popularServicesVOList = datas;
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
        View vv = inflater.inflate(R.layout.popular_adapter_itemview, parent, false);
        return new MyViewHolder(vv);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PopularServicesVO popularServicesVO = popularServicesVOList.get(position);

        holder.serviceName.setText(popularServicesVO.getName());

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it") || checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")) {
            if (MDetect.INSTANCE.isUnicode()) {

                Utility.addFontSuHome(holder.serviceName, popularServicesVO.getNameMm());

            } else {

                Utility.changeFontUni2ZgHome(holder.serviceName, popularServicesVO.getNameMm());
            }
        } else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")) {
            holder.serviceName.setText(popularServicesVO.getNameCh());
        } else {
            holder.serviceName.setText(popularServicesVO.getName());
        }

        if(Double.parseDouble(String.valueOf(popularServicesVO.getOriginalPrice())) == 0){
            holder.price.setVisibility(View.INVISIBLE);

        } else {
            holder.price.setVisibility(View.VISIBLE);
            holder.price.setText(NumberFormat.getNumberInstance(Locale.US).format(popularServicesVO.getOriginalPrice()) + " " + context.getString(R.string.currency));

        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);

        Glide.with(holder.itemView.getContext())
                .load(popularServicesVO.getPopularImage())
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
        return popularServicesVOList.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

}