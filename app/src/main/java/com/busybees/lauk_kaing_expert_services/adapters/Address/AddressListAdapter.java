package com.busybees.lauk_kaing_expert_services.adapters.Address;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busybees.lauk_kaing_expert_services.R;
import com.busybees.lauk_kaing_expert_services.activity.AddressActivity;
import com.busybees.lauk_kaing_expert_services.data.vos.Address.AddressVO;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    private View.OnClickListener onClickListener;
    int row_index = 0;
    int check_index = 0;
    Context context;
    List<AddressVO> addressVOList;
    public AddressActivity click;

    public AddressListAdapter(AddressActivity addressListActivity, List<AddressVO> data) {
        this.context = addressListActivity;
        this.addressVOList = data;
    }

    public void setCLick(AddressActivity addressListActivity) {
        this.click = addressListActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView edit;
        private ImageView delete;
        private TextView type;
        private TextView addressDetail;
        private ImageView checkbox;
        private RelativeLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);

            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            type = itemView.findViewById(R.id.addressType);
            addressDetail = itemView.findViewById(R.id.addressDetail);
            checkbox = itemView.findViewById(R.id.checkbox);
            card = itemView.findViewById(R.id.card_address);
        }
    }
    @Override
    public AddressListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = inflater.inflate(R.layout.address_item_adapter, parent, false);
        return new AddressListAdapter.MyViewHolder(vv);
    }
    @Override
    public void onBindViewHolder(final AddressListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AddressVO obj = addressVOList.get(position);

        String phoneNumber = "";

        if(holder != null && obj != null) {
            holder.type.setText(obj.getType());
            if(obj.getPhone()==null){
                phoneNumber  ="-";
            }else{
                phoneNumber =obj.getPhone();
            }

            String address = obj.getAddress();
            holder.addressDetail.setText(address);

            if(position==check_index) {
                holder.checkbox.setBackgroundDrawable(context.getDrawable(R.drawable.ic_check));
            } else {
                holder.checkbox.setBackgroundDrawable(context.getDrawable(R.drawable.ic_uncheck));
            }

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.EditAddress(obj, position);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.DeleteAddress(obj);
                }
            });

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check_index=position;
                    click.GetPosition(position);
                    notifyDataSetChanged();
                }
            });

            if (position==row_index){
                holder.delete.setVisibility(View.GONE);
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
        return addressVOList.size();
    }
}

