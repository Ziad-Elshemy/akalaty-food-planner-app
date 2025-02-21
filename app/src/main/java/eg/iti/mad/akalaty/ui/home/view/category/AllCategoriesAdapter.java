package eg.iti.mad.akalaty.ui.home.view.category;

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


public class AllCategoriesAdapter extends RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder> {

    private final Context context;
    private List<CategoriesItem> my_list;
    private static final String TAG = "RecyclerView";
    OnCategoryClickListener listener;

    public AllCategoriesAdapter(Context context, List<CategoriesItem> list, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cat,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesItem categoriesItem = my_list.get(position);
        holder.txtCategoryName.setText(my_list.get(position).getStrCategory());
        Glide.with(context).load(my_list.get(position).getStrCategoryThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgCategory);
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
        public ImageView imgCategory;
        public TextView txtCategoryName;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgCategory = layout.findViewById(R.id.imgCategoryItem);
            txtCategoryName = layout.findViewById(R.id.txtCategoryItem);

        }

    }

    public void changeData(List<CategoriesItem> categoriesItemList){
        this.my_list = categoriesItemList;
        notifyDataSetChanged();
    }


}
