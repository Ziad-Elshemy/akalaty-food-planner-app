package eg.iti.mad.akalaty.ui.meal_details.view;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
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



public class MealDetailsIngredientsAdapter extends RecyclerView.Adapter<MealDetailsIngredientsAdapter.ViewHolder> {

    private final Context context;
    private List<Pair<String,String>> my_list;
    private static final String TAG = "RecyclerView";

    public MealDetailsIngredientsAdapter(Context context, List<Pair<String,String>> list) {
        this.context = context;
        this.my_list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_meal_details_ingredients,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtIngredientName.setText(my_list.get(position).first);
        holder.txtIngredientMeasure.setText(my_list.get(position).second);
        Glide.with(context).load("https://www.themealdb.com/images/ingredients/"+my_list.get(position).first+"-Small.png")
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imgIngredient);

        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgIngredient;
        public TextView txtIngredientName;
        public TextView txtIngredientMeasure;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgIngredient = layout.findViewById(R.id.imgIngredientMealDetailsItem);
            txtIngredientName = layout.findViewById(R.id.txtIngredientMealDetailsItemTitle);
            txtIngredientMeasure = layout.findViewById(R.id.txtIngredientMealDetailsItemMeasure);

        }

    }

    public void changeData(List<Pair<String,String>> newList){
        this.my_list = newList;
        notifyDataSetChanged();
    }


}
