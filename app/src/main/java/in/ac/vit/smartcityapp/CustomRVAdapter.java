package in.ac.vit.smartcityapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class CustomRVAdapter extends RecyclerView.Adapter<CustomRVAdapter.CustomViewHolder>{

    private Context activityContext ;

    public CustomRVAdapter(Context activityContext) {
        this.activityContext = activityContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public CustomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
