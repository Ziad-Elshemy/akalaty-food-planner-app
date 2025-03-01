package eg.iti.mad.akalaty.ui.home.view.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.ui.home.view.OnMealClickListener;


public class MealsByCategoryAdapter extends RecyclerView.Adapter<MealsByCategoryAdapter.ViewHolder> {

    private final Context context;
    private List<FilteredMealsItem> my_list;
    private static final String TAG = "RecyclerView";
    OnMealClickListener listener;

    public MealsByCategoryAdapter(Context context, List<FilteredMealsItem> list, OnMealClickListener onMealClickListener) {
        this.context = context;
        this.my_list = list;
        this.listener = onMealClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cat_meals,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilteredMealsItem filteredMealsItem = my_list.get(position);
        holder.txtMealName.setText(my_list.get(position).getStrMeal());
        holder.lottieAnimationView.setVisibility(View.VISIBLE);
        Glide.with(context).load(my_list.get(position).getStrMealThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        holder.lottieAnimationView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        holder.lottieAnimationView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_logo)
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
        public TextView txtMealName;

        public LottieAnimationView lottieAnimationView;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            imgMeal = layout.findViewById(R.id.imgCatMealItem);
            txtMealName = layout.findViewById(R.id.txtCatMealName);
            lottieAnimationView = layout.findViewById(R.id.lottieAnimationView);

        }

    }

    public void changeData(List<FilteredMealsItem> filteredMealsItems){
        this.my_list = filteredMealsItems;
        notifyDataSetChanged();
    }


}
