package eg.iti.mad.akalaty.ui.search.view;

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
import eg.iti.mad.akalaty.ui.home.view.area.OnAreaClickListener;


public class AllAreasSearchAdapter extends RecyclerView.Adapter<AllAreasSearchAdapter.ViewHolder> {

    private final Context context;
    private List<AreasItem> my_list;
    private static final String TAG = "RecyclerView";
    OnAreaClickListener listener;

    public AllAreasSearchAdapter(Context context, List<AreasItem> list, OnAreaClickListener onAreaClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onAreaClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_search,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AreasItem areasItem = my_list.get(position);
        holder.txtItemSearch.setText(my_list.get(position).getStrArea());
        holder.imgItemSearch.setImageResource(AreasImages.getAreaByName(my_list.get(position).getStrArea()));
        holder.layout.setOnClickListener(view -> {
            listener.onAreaItemClicked(areasItem);
        });
        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgItemSearch;
        public TextView txtItemSearch;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgItemSearch = layout.findViewById(R.id.imgItemSearch);
            txtItemSearch = layout.findViewById(R.id.txtItemSearch);

        }

    }

    public void changeData(List<AreasItem> areasItemList){
        this.my_list = areasItemList;
        notifyDataSetChanged();
    }


}
