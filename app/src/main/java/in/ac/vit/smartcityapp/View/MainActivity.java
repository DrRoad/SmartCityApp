package in.ac.vit.smartcityapp.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.vit.smartcityapp.Controller.CustomRVAdapter;
import in.ac.vit.smartcityapp.Model.Entities.DeviceConfig;
import in.ac.vit.smartcityapp.R;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.activity_main_recyclerView)
    RecyclerView recyclerViewGrid ;

    private CustomRVAdapter customRVAdapter ;
    List<DeviceConfig> deviceConfigList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this) ;
        init() ;

    }

    private void init() {
        addDataToArray() ;
        customRVAdapter = new CustomRVAdapter(this, deviceConfigList) ;
        recyclerViewGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewGrid.setAdapter(customRVAdapter);


    }

    private void addDataToArray() {
        deviceConfigList = new ArrayList<>() ;
        deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        deviceConfigList.add(new DeviceConfig(1, "Washing Machine", false, "offline")) ;
        deviceConfigList.add(new DeviceConfig(1, "Television", false, "last: 1 hour")) ;
        deviceConfigList.add(new DeviceConfig(1, "Main Lights", true, "220W")) ;
        deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        for (int i = 0; i < 12; i++) {
            deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        }
    }
}
