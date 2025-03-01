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
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.ui.home.view.OnMealClickListener;


public class MealBySelectorSearchAdapter extends RecyclerView.Adapter<MealBySelectorSearchAdapter.ViewHolder> {

    private final Context context;
    private List<FilteredMealsItem> my_list;
    private static final String TAG = "RecyclerView";
    OnMealClickListener listener;

    public MealBySelectorSearchAdapter(Context context, List<FilteredMealsItem> list, OnMealClickListener onMealClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onMealClickListener;
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
        FilteredMealsItem filteredMealsItem = my_list.get(position);
        holder.txtItemSearch.setText(my_list.get(position).getStrMeal());
        Glide.with(context).load(my_list.get(position).getStrMealThumb())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imgItemSearch);
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

    public void changeData(List<FilteredMealsItem> filteredMealsItems){
        this.my_list = filteredMealsItems;
        notifyDataSetChanged();
    }


}
