package eg.iti.mad.akalaty.ui.favorites.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.model.SingleMealItem;

public class MyFavAdapter extends RecyclerView.Adapter<MyFavAdapter.ViewHolder> {

    private final Context context;
    private List<SingleMealItem> my_list;
    private static final String TAG = "RecyclerView";

    OnRemoveClickListener listener;

    public MyFavAdapter(Context context, List<SingleMealItem> list, OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_fav_meals,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleMealItem singleMealItem = my_list.get(position);
        Glide.with(context).load(singleMealItem.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgMealImage);
        holder.txtMealName.setText(my_list.get(position).getStrMeal());
        holder.txtMealCat.setText(my_list.get(position).getStrCategory());
        holder.btnRemoveItem.setOnClickListener(view -> {
            listener.onRemoveClicked(singleMealItem);
        });
        holder.constraintLayout.setOnClickListener(view -> {

        });
        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgMealImage;
        public TextView txtMealName;
        public TextView txtMealCat;
        public ImageButton btnRemoveItem;
        public CardView constraintLayout;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgMealImage = layout.findViewById(R.id.imgFavMealItemImage);
            txtMealName = layout.findViewById(R.id.txtFavMealItemName);
            txtMealCat = layout.findViewById(R.id.txtFavMealItemCat);
            btnRemoveItem = layout.findViewById(R.id.btnRemoveFavMeal);
            constraintLayout = layout.findViewById(R.id.row_item);
        }

    }

    void changeData(List<SingleMealItem> newList){
        my_list = newList;
        notifyDataSetChanged();
    }

}
