package eg.iti.mad.akalaty.ui.home.view.area;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.model.AreasImages;
import eg.iti.mad.akalaty.model.AreasItem;


public class AllAreasAdapter extends RecyclerView.Adapter<AllAreasAdapter.ViewHolder> {

    private final Context context;
    private List<AreasItem> my_list;
    private static final String TAG = "RecyclerView";
    OnAreaClickListener listener;

    public AllAreasAdapter(Context context, List<AreasItem> list, OnAreaClickListener onAreaClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onAreaClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_area,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AreasItem categoriesItem = my_list.get(position);
        holder.txtAreaName.setText(my_list.get(position).getStrArea());
        holder.imgArea.setImageResource(AreasImages.getAreaByName(my_list.get(position).getStrArea()));
        holder.layout.setOnClickListener(view -> {
            listener.onAreaItemClicked(categoriesItem);
        });
        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgArea;
        public TextView txtAreaName;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgArea = layout.findViewById(R.id.imgAreaItem);
            txtAreaName = layout.findViewById(R.id.txtAreaItem);

        }

    }

    public void changeData(List<AreasItem> categoriesItemList){
        this.my_list = categoriesItemList;
        notifyDataSetChanged();
    }


}
