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


public class MealDetailsInstructionsAdapter extends RecyclerView.Adapter<MealDetailsInstructionsAdapter.ViewHolder> {

    private final Context context;
    private List<String> my_list;
    private static final String TAG = "RecyclerView";

    public MealDetailsInstructionsAdapter(Context context, List<String> list) {
        this.context = context;
        this.my_list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_meal_instructions,parent,false);
        ViewHolder vh = new ViewHolder(view);
        Log.i(TAG, "=========onCreateViewHolder=========");
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtIngredientCounter.setText((position+1)+"");
        holder.txtIngredientDesc.setText(my_list.get(position));

        Log.i(TAG, "=========onBindViewHolder===========");

    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtIngredientCounter;
        public TextView txtIngredientDesc;

        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            txtIngredientCounter = layout.findViewById(R.id.txtInstructionsCounter);
            txtIngredientDesc = layout.findViewById(R.id.txtMealInstructionItemDesc);

        }

    }

    public void changeData(List<String> newList){
        this.my_list = newList;
        notifyDataSetChanged();
    }


}
