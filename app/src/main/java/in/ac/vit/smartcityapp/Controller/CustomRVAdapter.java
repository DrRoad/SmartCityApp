package in.ac.vit.smartcityapp.Controller;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.Switch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.vit.smartcityapp.Model.Entities.DeviceConfig;
import in.ac.vit.smartcityapp.R;

public class CustomRVAdapter extends RecyclerView.Adapter<CustomRVAdapter.CustomViewHolder>{

    private static final String TAG = "TAG";
    private Context activityContext ;
    private List<DeviceConfig> deviceConfigList ;

    public CustomRVAdapter(Context activityContext, List<DeviceConfig> deviceConfigList) {
        this.activityContext = activityContext;
        this.deviceConfigList = deviceConfigList ;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        final DeviceConfig tempConfig = deviceConfigList.get(holder.getAdapterPosition()) ;
        holder.switchToggle.setChecked(tempConfig.isDeviceCurrentStatus());
        holder.tvDeviceName.setText(tempConfig.getDeviceName());
        holder.tvDeviceSpecialMessage.setText(tempConfig.getSpecialDescription());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempConfig.setDeviceId(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceConfigList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.list_item_device_name) TextView tvDeviceName ;
        @BindView(R.id.list_item_device_icon) ImageView ivDeviceIcon ;
        @BindView(R.id.list_item_device_special_attribute) TextView tvDeviceSpecialMessage ;
        @BindView(R.id.list_item_switch_) Switch switchToggle ;
        @BindView(R.id.list_item_linear_layout) LinearLayout linearLayout ;

        public CustomViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView) ;
        }


    }
}
