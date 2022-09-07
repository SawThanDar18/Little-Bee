package com.busybees.lauk_kaing_expert_services.adapters.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.NotificationActivity;
import com.busybees.lauk_kaing_expert_services.data.models.Notifications.NotificationDataModel;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.List;

import me.myatminsoe.mdetect.MDetect;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    List<NotificationDataModel> list;

    public NotificationAdapter(NotificationActivity notificationActivity, List<NotificationDataModel> data) {
        this.context=notificationActivity;
        this.list=data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView content,title,date;

        public MyViewHolder(View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.content);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.notification_adapter, parent, false);
        return new MyViewHolder(vv);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        NotificationDataModel obj = list.get(position);

        if(holder != null && obj != null) {

            String[] arr=obj.getCreatedAt().split(" ");
            holder.date.setText(arr[0]);

            if ( MDetect.INSTANCE.isUnicode()){
                Utility.addFontSu(holder.title, obj.getTitle());
                Utility.addFontSu(holder.content, obj.getContent());
            } else  {
                Utility.changeFontUni2Zg(holder.title, obj.getTitle());
                Utility.changeFontUni2Zg(holder.content, obj.getContent());
            }


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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}

