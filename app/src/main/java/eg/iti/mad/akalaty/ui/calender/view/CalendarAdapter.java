package eg.iti.mad.akalaty.ui.calender.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.model.PlannedMeal;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final Context context;
    private List<PlannedMeal> my_list;
    private static final String TAG = "RecyclerView";

    OnRemovePlannedClickListener listener;

    public CalendarAdapter(Context context, List<PlannedMeal> list, OnRemovePlannedClickListener onRemovePlannedClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onRemovePlannedClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cal_meals,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlannedMeal plannedMeal = my_list.get(position);
        Glide.with(context).load(plannedMeal.getMeal().getStrMealThumb())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imgMealImage);
        holder.txtMealName.setText(my_list.get(position).getMeal().getStrMeal());
        holder.txtMealCat.setText(my_list.get(position).getMeal().getStrCategory());
        holder.btnRemoveItem.setOnClickListener(view -> {
            listener.onRemovePlannedClicked(plannedMeal);
        });
        holder.constraintLayout.setOnClickListener(view -> {
            listener.onItemClicked(plannedMeal);
        });
        Animation fromLeft = AnimationUtils.loadAnimation(context,R.anim.from_left_anim);
        holder.constraintLayout.setAnimation(fromLeft);
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
            imgMealImage = layout.findViewById(R.id.imgCalMealItemImage);
            txtMealName = layout.findViewById(R.id.txtCalMealItemName);
            txtMealCat = layout.findViewById(R.id.txtCalMealItemCat);
            btnRemoveItem = layout.findViewById(R.id.btnRemoveCalMeal);
            constraintLayout = layout.findViewById(R.id.row_item);
        }

    }

    void changeData(List<PlannedMeal> newList){
        my_list = newList;
        notifyDataSetChanged();
    }

}
