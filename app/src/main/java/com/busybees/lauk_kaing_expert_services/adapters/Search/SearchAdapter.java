package com.busybees.lauk_kaing_expert_services.adapters.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.SearchActivity;
import com.busybees.lauk_kaing_expert_services.data.models.Search.SearchDataModel;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context mContext;
    List<SearchDataModel> listDataModel = new ArrayList<SearchDataModel>();
    public SearchActivity click;

    public SearchAdapter(SearchActivity searchActivity, ArrayList<SearchDataModel> searchlist) {
        this.mContext=searchActivity;
        this.listDataModel=searchlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SearchDataModel objDataModel = listDataModel.get(position);
        mContext=holder.itemView.getContext();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(mContext, 8)));

        Glide.with(holder.itemView.getContext())
                .load(objDataModel.getImage())
                .apply(requestOptions)
                .into(holder.catIcon);

        if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("it") || checkLng(holder.itemView.getContext()).equalsIgnoreCase("fr")){
            if ( MDetect.INSTANCE.isUnicode()){
                Utility.addFontSu(holder.catTextView,objDataModel.getNameMm());

            } else  {
                Utility.changeFontUni2Zg(holder.catTextView,objDataModel.getNameMm());
            }
        }else if (checkLng(holder.itemView.getContext()).equalsIgnoreCase("zh")){
            holder.catTextView.setText(objDataModel.getNameCh());
        } else {
            holder.catTextView.setText(objDataModel.getName());
        }


        holder.search_view_id.setOnClickListener(v -> {

            if (objDataModel.getStep() == 1){

                click.IntentServiceDetailView(objDataModel);

            }else if (objDataModel.getStep() == 2) {

                click.IntentSubProductView(objDataModel);

            } else {
                click.IntentProductView(objDataModel);
            }
        });
    }
    @Override
    public int getItemCount() {
        return listDataModel.size();
    }


    public void setClick(SearchActivity searchActivity) {
        this.click=searchActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView catTextView;
        private ImageView catIcon;
        private CardView search_view_id;

        public MyViewHolder(View view) {
            super(view);
            catIcon = (ImageView) view.findViewById(R.id.catIcon);
            catTextView = (TextView) view.findViewById(R.id.catTextView);
            search_view_id=(CardView) view.findViewById(R.id.search_view_id);

        }
    }
    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}


