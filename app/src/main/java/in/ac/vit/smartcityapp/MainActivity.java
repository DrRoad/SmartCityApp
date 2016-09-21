package in.ac.vit.smartcityapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.activity_main_recyclerView)
    RecyclerView recyclerViewGrid ;

    private CustomRVAdapter customRVAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this) ;

        init() ;
    }

    private void init() {
        customRVAdapter = new CustomRVAdapter(this) ;
        recyclerViewGrid.setAdapter(customRVAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this) ;

    }
}
