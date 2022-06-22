package com.busybees.lauk_kaing_expert_services.adapters.AddMoreServices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.utility.Utility;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    public ExpandableListViewAdapter(FragmentActivity activity) {
        this.context=activity;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return childPosititon;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_child_item_view, null);
        }

        ImageView productImage = convertView.findViewById(R.id.productIcon);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));

        Glide.with(convertView.getContext())
                .load("https://busybeesexpertservice.com/upload/product_images_new/CS_cleaning_upholstery_sofa.png")
                .apply(requestOptions)
                .into(productImage);
        notifyDataSetChanged();

        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return 4;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_group_item_view, null);
        }

        ImageView imageView=convertView.findViewById(R.id.productIcon);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loader_circle_shape);
        requestOptions.error(R.drawable.loader_circle_shape);
        requestOptions.transforms(new CenterCrop(), new RoundedCorners(Utility.dp2px(context, 8)));

        Glide.with(convertView.getContext())
                .load("https://busybeesexpertservice.com/upload/product_images_new/CS_cleaning_upholstery_sofa.png")
                .apply(requestOptions)
                .into(imageView);

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

}
