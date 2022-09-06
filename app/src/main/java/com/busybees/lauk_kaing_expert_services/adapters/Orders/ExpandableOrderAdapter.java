package com.busybees.lauk_kaing_expert_services.adapters.Orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.OrderDetailActivity;
import com.busybees.lauk_kaing_expert_services.data.models.MyOrders.MyOrderDataModel;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.MyOrdersDetailVO;
import com.busybees.lauk_kaing_expert_services.data.vos.MyOrders.VendorInfoVO;
import com.busybees.lauk_kaing_expert_services.fragments.OrdersFragment;
import com.busybees.lauk_kaing_expert_services.network.NetworkServiceProvider;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

public class ExpandableOrderAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MyOrderDataModel> listDataGroup;
    private List<MyOrdersDetailVO> listDataChild;

    private String vinfo = "";

    OrdersFragment fragment = new OrdersFragment();

    public ExpandableOrderAdapter(Context context, ArrayList<MyOrderDataModel> listDataGroup,
                                  ArrayList<MyOrdersDetailVO> listChildData) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataGroup.get(groupPosition).getOrdersDetail().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        MyOrdersDetailVO listChild = listDataGroup.get(groupPosition).getOrdersDetail().get(childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_expandable_list_child_view, null);
        }

        TextView orderDateTime = convertView.findViewById(R.id.order_date_time);
        TextView orderDetailId = convertView.findViewById(R.id.order_id);
        TextView serviceName = convertView.findViewById(R.id.serviceName);
        TextView serviceAmt = convertView.findViewById(R.id.serviceAmt);
        LinearLayout layoutStatus = convertView.findViewById(R.id.layout_status);
        TextView serviceStatus = convertView.findViewById(R.id.serviceStatus);

        RelativeLayout orderDetailLayout = convertView.findViewById(R.id.orderDetailLayout);

        orderDateTime.setText(listChild.getTime() + "( " + listChild.getDate() + " )");
        orderDetailId.setText(String.valueOf(listChild.getOrderDetailId()));

        for (int i = 0; i < listChild.getProductPrice().size(); i++) {
            if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")){
                if ( MDetect.INSTANCE.isUnicode()){
                    Utility.addFontSu(serviceName, listChild.getProductPrice().get(i).getServiceNameMm());
                } else  {
                    Utility.changeFontUni2Zg(serviceName, listChild.getProductPrice().get(i).getServiceNameMm());
                }
            } else if (checkLng(context).equalsIgnoreCase("zh")) {
                serviceName.setText(listChild.getProductPrice().get(i).getServiceNameCh());
            }
            else{
                serviceName.setText(listChild.getProductPrice().get(i).getServiceName());
            }
        }

        getVendorInfo(listChild.getVendorData());

        if (listChild.getStatus().equalsIgnoreCase("Upcoming")) {
            serviceStatus.setText("Confirmed");
        } else {
            serviceStatus.setText(listChild.getStatus());
        }

        if (listChild.getTotalPrice() == 0) {
            serviceAmt.setText("Survey");
        } else {
            serviceAmt.setText(context.getString(R.string.currency) + " " + listChild.getTotalPrice());
        }

        if (String.valueOf(listChild.getStatus()).equalsIgnoreCase(context.getString(R.string.Idle))) {
            layoutStatus.setBackgroundResource(R.color.idle);
        }  else if (String.valueOf(listChild.getStatus()).equalsIgnoreCase(context.getString(R.string.Ongoing))) {
            layoutStatus.setBackgroundResource(R.color.ongoing);
        } else if (String.valueOf(listChild.getStatus()).equalsIgnoreCase(context.getString(R.string.OCancel))) {
            layoutStatus.setBackgroundResource(R.color.cancel);
        }else if (String.valueOf(listChild.getStatus()).equalsIgnoreCase("Upcoming")){
            layoutStatus.setBackgroundResource(R.color.confirm);
        }else {
            layoutStatus.setBackgroundResource(R.color.testing);
        }

        layoutStatus.setPadding(0,context.getResources().getDimensionPixelOffset(R.dimen.margin6),
                0,context.getResources().getDimensionPixelSize(R.dimen.margin6));


        orderDetailLayout.setOnClickListener(v -> {
            Intent intent=new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_detail", listChild);
            context.startActivity(intent);
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataGroup.get(groupPosition).getOrdersDetail().size();
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

        MyOrderDataModel groupList = listDataGroup.get(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.order_expandable_list_group_view, null);
        }

        TextView order_created_time_date = convertView.findViewById(R.id.order_created_time_date);
        TextView reference_no = convertView.findViewById(R.id.reference_no);

        order_created_time_date.setText(groupList.getOrderCreatedDate());
        reference_no.setText("Reference No - " + groupList.getReferenceNo());

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

    public String getVendorInfo(List<VendorInfoVO> vobj){

        vinfo = "";
        if(vobj != null && !vobj.isEmpty()) {

            for (int i = 0; i <vobj.size() ; i++) {

                vinfo=vinfo+"<span style=\"color:#7f7f7f;\">" +"<br><b>NAME</b> : "+vobj.get(i).getVendorName() +"<br><b>PHONE :</b> "+vobj.get(i).getVendorPhone() + "<br/><br/>";

            }

        }else {

        }

        return vinfo.trim();

    }
}
