package eg.iti.mad.akalaty.ui.home.view.ingredient;

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
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.ui.home.view.OnHeartClickListener;
import eg.iti.mad.akalaty.ui.home.view.OnMealClickListener;


public class MealsByIngredientAdapter extends RecyclerView.Adapter<MealsByIngredientAdapter.ViewHolder> {

    private final Context context;
    private List<FilteredMealsItem> my_list;
    private static final String TAG = "RecyclerView";
    OnMealClickListener listener;
    OnHeartClickListener onHeartClickListener;

    public MealsByIngredientAdapter(Context context, List<FilteredMealsItem> list, OnMealClickListener onMealClickListener, OnHeartClickListener onHeartClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onMealClickListener;
        this.onHeartClickListener = onHeartClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient_meals,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilteredMealsItem filteredMealsItem = my_list.get(position);
        holder.txtMealName.setText(my_list.get(position).getStrMeal());
        holder.imgHeart.setOnClickListener(view -> {
            view.setBackgroundResource(R.drawable.ic_heart_red);
            onHeartClickListener.onHeartIconClicked(filteredMealsItem);
        });
        Glide.with(context).load(my_list.get(position).getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgMeal);
        holder.layout.setOnClickListener(view -> {
            listener.onMealItemClicked(filteredMealsItem);
        });
        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgMeal;
        public ImageView imgHeart;

        public TextView txtMealName;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgMeal = layout.findViewById(R.id.imgIngredientMealItem);
            imgHeart = layout.findViewById(R.id.imgHeartIcon);
            txtMealName = layout.findViewById(R.id.txtIngredientMealName);

        }

    }

    public void changeData(List<FilteredMealsItem> filteredMealsItems){
        this.my_list = filteredMealsItems;
        notifyDataSetChanged();
    }


}
