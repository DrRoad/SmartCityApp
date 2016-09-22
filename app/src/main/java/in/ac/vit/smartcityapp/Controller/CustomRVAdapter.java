package in.ac.vit.smartcityapp.Controller;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import in.ac.vit.smartcityapp.Model.Interfaces.ActivityToAdapterCom;
import in.ac.vit.smartcityapp.R;
import in.ac.vit.smartcityapp.View.MainActivity;

public class CustomRVAdapter extends RecyclerView.Adapter<CustomRVAdapter.CustomViewHolder> implements ActivityToAdapterCom {

    private static final String TAG = "TAG";
    private MainActivity mainActivity;
    private List<DeviceConfig> deviceConfigList ;
    private CustomViewHolder holder ;

    public CustomRVAdapter(Context activityContext, List<DeviceConfig> deviceConfigList) {
        mainActivity = (MainActivity) activityContext ;
        this.deviceConfigList = deviceConfigList ;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view) ;
        holder = customViewHolder ;
        return holder;
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

        holder.switchToggle.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                Log.i(TAG, "onCheckedChanged: ");
                mainActivity.notifyOnServer(holder.getAdapterPosition(), checked);
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

    @Override
    public void toggleChange(int i, boolean status) {
        DeviceConfig tempConfig = deviceConfigList.get(i) ;
        tempConfig.setDeviceCurrentStatus(status);
        Log.i(TAG, "toggleChange: ");
        holder.switchToggle.setChecked(status);
    }
}
