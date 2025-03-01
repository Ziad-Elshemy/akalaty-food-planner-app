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
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.ui.home.view.category.OnCategoryClickListener;


public class AllCategoriesSearchAdapter extends RecyclerView.Adapter<AllCategoriesSearchAdapter.ViewHolder> {

    private final Context context;
    private List<CategoriesItem> my_list;
    private static final String TAG = "RecyclerView";
    OnCategoryClickListener listener;

    public AllCategoriesSearchAdapter(Context context, List<CategoriesItem> list, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onCategoryClickListener;
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
        CategoriesItem categoriesItem = my_list.get(position);
        holder.txtItemSearch.setText(my_list.get(position).getStrCategory());
        Glide.with(context).load(my_list.get(position).getStrCategoryThumb())
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(holder.imgItemSearch);
        holder.layout.setOnClickListener(view -> {
            listener.onCategoryItemClicked(categoriesItem);
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

    public void changeData(List<CategoriesItem> categoriesItemList){
        this.my_list = categoriesItemList;
        notifyDataSetChanged();
    }


}
