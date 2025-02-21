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

import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.model.IngredientsItem;



public class AllIngredientsAdapter extends RecyclerView.Adapter<AllIngredientsAdapter.ViewHolder> {

    private final Context context;
    private List<IngredientsItem> my_list;
    private static final String TAG = "RecyclerView";
    OnIngredientClickListener listener;

    public AllIngredientsAdapter(Context context, List<IngredientsItem> list, OnIngredientClickListener onIngredientClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onIngredientClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientsItem categoriesItem = my_list.get(position);
        holder.txtIngredientName.setText(my_list.get(position).getStrIngredient());
        holder.imgIngredient.setImageResource(R.drawable.favorite_ic);
        holder.layout.setOnClickListener(view -> {
            listener.onIngredientItemClicked(categoriesItem);
        });
        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgIngredient;
        public TextView txtIngredientName;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgIngredient = layout.findViewById(R.id.imgIngredientItem);
            txtIngredientName = layout.findViewById(R.id.txtIngredientItem);

        }

    }

    public void changeData(List<IngredientsItem> categoriesItemList){
        this.my_list = categoriesItemList;
        notifyDataSetChanged();
    }


}
