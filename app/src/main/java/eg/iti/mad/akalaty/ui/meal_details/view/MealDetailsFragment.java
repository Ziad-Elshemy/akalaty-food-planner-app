package eg.iti.mad.akalaty.ui.meal_details.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentMealDetailsBinding;
import eg.iti.mad.akalaty.model.AreasImages;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.meal_details.presenter.MealDetailsPresenter;


public class MealDetailsFragment extends Fragment implements IViewMealDetailsFragment {

    String mealId;
    FragmentMealDetailsBinding viewDataBinding;
    MealDetailsPresenter mealDetailsPresenter;
    MealDetailsIngredientsAdapter mealDetailsIngredientsAdapter;
    MealDetailsInstructionsAdapter mealDetailsInstructionsAdapter;

    private static final String TAG = "MealDetailsFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meal_details,container,false);

        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealId = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealId();
        mealDetailsIngredientsAdapter = new MealDetailsIngredientsAdapter(requireContext(),new ArrayList<>());
        mealDetailsInstructionsAdapter = new MealDetailsInstructionsAdapter(requireContext(),new ArrayList<>());
        viewDataBinding.recyclerViewMealIngredientsList.setAdapter(mealDetailsIngredientsAdapter);
        viewDataBinding.recyclerViewMealInstructionList.setAdapter(mealDetailsInstructionsAdapter);
        mealDetailsPresenter = new MealDetailsPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));
        mealDetailsPresenter.getMailById(mealId);




    }

    private List<Pair<String, String>> createIngredientPairs(SingleMealItem meal) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        try {
            for (int i = 1; i <= 20; i++) {
                Field ingredientField = SingleMealItem.class.getDeclaredField("strIngredient" + i);
                Field measureField = SingleMealItem.class.getDeclaredField("strMeasure" + i);
                ingredientField.setAccessible(true);
                measureField.setAccessible(true);

                String ingredient = (String) ingredientField.get(meal);
                String measure = (String) measureField.get(meal);

                if (ingredient != null && !ingredient.isEmpty()) {
                    measure = (measure != null && !measure.isEmpty() && !measure.trim().equals("")) ? measure : "N/A";
                    pairList.add(Pair.create(ingredient, measure));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return pairList;
    }

    private void setupYoutubeVideo(SingleMealItem meal) {
        getLifecycle().addObserver(viewDataBinding.youtubeVideo);
        String mealVideoId = meal.getStrYoutube().substring(meal.getStrYoutube().indexOf('=') + 1);
        viewDataBinding.youtubeVideo.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(mealVideoId, 0);
            }
        });
    }

    @Override
    public void showMealDetails(SingleMealItem singleMealItem) {
        Log.i(TAG, "showMealDetails: " + singleMealItem);
        viewDataBinding.txtMealDetailsName.setText(singleMealItem.getStrMeal());
        viewDataBinding.txtMealDetailsCat.setText(singleMealItem.getStrCategory());
        Glide.with(requireActivity()).load(singleMealItem.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(viewDataBinding.imgMealDetailsImage);
        viewDataBinding.imgMealDetailsArea.setImageResource(AreasImages.getAreaByName(singleMealItem.getStrArea()));
        Toast.makeText(requireContext(), "ziad success meal details", Toast.LENGTH_SHORT).show();

        mealDetailsIngredientsAdapter.changeData(createIngredientPairs(singleMealItem));
        List<String> instructionsList = Arrays.asList(singleMealItem.getStrInstructions().split("\r\n"));
        mealDetailsInstructionsAdapter.changeData(instructionsList);

        setupYoutubeVideo(singleMealItem);

//        //convert image to bitmap to add it to database
//        Glide.with(this)
//                .asBitmap()
//                .load(singleMealItem.getStrMealThumb())
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        Bitmap bitmap = resource;
//                        singleMealItem.setImage(bitmap);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });



        //add meal to database
        viewDataBinding.icMealDetailsHeart.setOnClickListener(view1 -> {
            mealDetailsPresenter.addMealToFav(singleMealItem);
        });

    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Toast.makeText(requireContext(), "error meal details", Toast.LENGTH_SHORT).show();
    }
}