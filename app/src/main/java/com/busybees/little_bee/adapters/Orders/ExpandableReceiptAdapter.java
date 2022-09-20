package com.busybees.little_bee.adapters.Orders;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.busybees.little_bee.R;
import com.busybees.little_bee.activity.HistoryDetailActivity;
import com.busybees.little_bee.data.models.MyHistory.MyHistoryDataModel;
import com.busybees.little_bee.data.vos.MyHistory.MyHistoryDetailVO;
import com.busybees.little_bee.data.vos.MyHistory.QuestionsVO;
import com.busybees.little_bee.data.vos.MyOrders.ProductPriceVO;
import com.busybees.little_bee.utility.AppENUM;
import com.busybees.little_bee.utility.AppStorePreferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

public class ExpandableReceiptAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MyHistoryDataModel> listDataGroup;
    private List<MyHistoryDetailVO> listDataChild;
    private List<QuestionsVO> questionList;

    public ExpandableReceiptAdapter(Context context, ArrayList<MyHistoryDataModel> listDataGroup,
                                    ArrayList<MyHistoryDetailVO> listChildData, ArrayList<QuestionsVO> qList) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
        this.questionList = qList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataGroup.get(groupPosition).getMyHistoryDetail().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        MyHistoryDetailVO listchild = listDataGroup.get(groupPosition).getMyHistoryDetail().get(childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.receipt_expandable_list_child_view, null);
        }

        LinearLayout order_layout = convertView.findViewById(R.id.order_layout);
        TextView order_id = convertView.findViewById(R.id.order_id);
        TextView serviceName = convertView.findViewById(R.id.serviceName);
        TextView order_count = convertView.findViewById(R.id.order_count);
        RatingBar rating_bar_small = convertView.findViewById(R.id.rating_bar_small);
        TextView totalTxt = convertView.findViewById(R.id.total_txt);
        TextView total = convertView.findViewById(R.id.total);

        order_layout.setOnClickListener(v -> {
            Intent intent=new Intent(context, HistoryDetailActivity.class);
            intent.putExtra("receipt_data", listchild);
            intent.putExtra("question", (Serializable) questionList);
            context.startActivity(intent);
        });

        if (listchild != null) {
            List<ProductPriceVO> productModels = listchild.getProductPrice();
            if (productModels.size() > 2) {
                serviceName.setText(getProductNames(productModels));
                serviceName.setEllipsize(TextUtils.TruncateAt.END);
            } else {
                serviceName.setText(getProductNames(productModels));
            }

            order_id.setText(String.valueOf(listchild.getOrderDetailId()));

            if (listchild.getOverAllRating() != 0) {
                rating_bar_small.setVisibility(View.VISIBLE);
                rating_bar_small.setRating(listchild.getOverAllRating());
            } else {
                rating_bar_small.setVisibility(View.GONE);
            }

            order_count.setText(String.valueOf(productModels.size()));

            if (listchild.getTotalPrice() != 0) {
                totalTxt.setVisibility(View.VISIBLE);
                total.setText(context.getString(R.string.currency) + " " + listchild.getTotalPrice());
            } else {
                totalTxt.setVisibility(View.GONE);
                total.setText(context.getString(R.string.survey_label));
            }

        } else {
            serviceName.setText("No Data");
        }

        return convertView;
    }

    public String getProductNames(List<ProductPriceVO> productModels){
        String productName = "";
        for (int i = 0; i < productModels.size(); i++) {
            ProductPriceVO pmodel = productModels.get(i);
            if(pmodel != null){
                if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")){
                    if ( MDetect.INSTANCE.isUnicode()){
                        productName = productName +". " +  pmodel.getServiceNameMm() + "\n";

                    } else  {
                        productName = productName +". " +  pmodel.getServiceNameMm() + "\n";
                    }
                } else if (checkLng(context).equalsIgnoreCase("zh")){
                    productName = productName +". " +  pmodel.getServiceNameCh() + "\n";
                }
                else{
                    productName = productName +". " +  pmodel.getServiceName() + "\n";
                }

            }
        }
        return productName.trim();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataGroup.get(groupPosition).getMyHistoryDetail().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        MyHistoryDataModel grouplist = listDataGroup.get(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.receipt_expandable_list_group_view, null);
        }

        TextView order_completed_time_date = convertView.findViewById(R.id.order_completed_time_date);
        if (grouplist.getOrderCompletedDate() == null) {
            order_completed_time_date.setText("2022-08-26");
        } else {
            order_completed_time_date.setText(grouplist.getOrderCompletedDate());
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return super.getCombinedChildId(groupId, childId);
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }
}
