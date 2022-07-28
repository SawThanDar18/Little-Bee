package com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.ProductsVO;
import com.busybees.lauk_kaing_expert_services.data.vos.ServiceDetail.SubProductsVO;
import com.busybees.lauk_kaing_expert_services.utility.AppENUM;
import com.busybees.lauk_kaing_expert_services.utility.AppStorePreferences;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import me.myatminsoe.mdetect.MDetect;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ProductsVO> listDataGroup;
    private List<SubProductsVO> listDataChild;

    public ExpandableListViewAdapter(FragmentActivity activity, ArrayList<ProductsVO> productList, ArrayList<SubProductsVO> subproductList) {
        this.context=activity;
        listDataGroup = productList;
        listDataChild = subproductList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataGroup.get(groupPosition).getSubProducts().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        SubProductsVO listChild = listDataGroup.get(groupPosition).getSubProducts().get(childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_child_item_view, null);
        }

        ImageView subProductImage = convertView.findViewById(R.id.productIcon);
        TextView subProductName = convertView.findViewById(R.id.productName);

        if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")) {
            if (MDetect.INSTANCE.isUnicode()) {

                Utility.addFontSuHome(subProductName, listChild.getNameMm());

            } else {

                Utility.changeFontUni2ZgHome(subProductName, listChild.getNameMm());
            }
        } else if (checkLng(context).equalsIgnoreCase("zh")) {
            subProductName.setText(listChild.getNameCh());
        } else {
            subProductName.setText(listChild.getName());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));

        Glide.with(convertView.getContext())
                .load(listChild.getSubProductImage())
                .apply(requestOptions)
                .into(subProductImage);
        notifyDataSetChanged();

        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataGroup.get(groupPosition).getSubProducts().size();
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

        ProductsVO groupList = listDataGroup.get(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_group_item_view, null);
        }

        ImageView productImage = convertView.findViewById(R.id.productIcon);
        TextView productName = convertView.findViewById(R.id.productName);

        if (checkLng(context).equalsIgnoreCase("it") || checkLng(context).equalsIgnoreCase("fr")) {
            if (MDetect.INSTANCE.isUnicode()) {

                Utility.addFontSuHome(productName, groupList.getNameMm());

            } else {

                Utility.changeFontUni2ZgHome(productName, groupList.getNameMm());
            }
        } else if (checkLng(context).equalsIgnoreCase("zh")) {
            productName.setText(groupList.getNameCh());
        } else {
            productName.setText(groupList.getName());
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));

        Glide.with(convertView.getContext())
                .load(groupList.getProductImage())
                .apply(requestOptions)
                .into(productImage);

        notifyDataSetChanged();

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
    public long getCombinedGroupId(long groupId) {
        return super.getCombinedGroupId(groupId);
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
