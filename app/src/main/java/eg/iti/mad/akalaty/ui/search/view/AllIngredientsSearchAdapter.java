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
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.ui.home.view.ingredient.OnIngredientClickListener;


public class AllIngredientsSearchAdapter extends RecyclerView.Adapter<AllIngredientsSearchAdapter.ViewHolder> {

    private final Context context;
    private List<IngredientsItem> my_list;
    private static final String TAG = "RecyclerView";
    OnIngredientClickListener listener;

    public AllIngredientsSearchAdapter(Context context, List<IngredientsItem> list, OnIngredientClickListener onIngredientClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onIngredientClickListener;
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
        IngredientsItem ingredientsItem = my_list.get(position);
        holder.txtItemSearch.setText(my_list.get(position).getStrIngredient());
        Glide.with(context).load("https://www.themealdb.com/images/ingredients/"+my_list.get(position).getStrIngredient()+"-Small.png")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgItemSearch);
        holder.layout.setOnClickListener(view -> {
            listener.onIngredientItemClicked(ingredientsItem);
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

    public void changeData(List<IngredientsItem> ingredientsItemList){
        this.my_list = ingredientsItemList;
        notifyDataSetChanged();
    }


}
