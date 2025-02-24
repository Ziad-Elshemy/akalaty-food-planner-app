package eg.iti.mad.akalaty.ui.meal_details.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.Utils;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentMealDetailsBinding;
import eg.iti.mad.akalaty.model.AreasImages;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.meal_details.presenter.MealDetailsPresenter;


public class MealDetailsFragment extends Fragment implements IViewMealDetailsFragment {

    String mealId;
    FragmentMealDetailsBinding viewDataBinding;
    MealDetailsPresenter mealDetailsPresenter;
    MealDetailsIngredientsAdapter mealDetailsIngredientsAdapter;
    MealDetailsInstructionsAdapter mealDetailsInstructionsAdapter;

    boolean isFavorite;
    Calendar calendar;

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
        isFavorite = false;
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

        //add meal to database
        viewDataBinding.icMealDetailsHeart.setOnClickListener(view1 -> {
            isFavorite = !isFavorite;
            if(isFavorite){
                Utils.showCustomSnackbar(requireView(),"Added to favorite");
                viewDataBinding.icMealDetailsHeart.setImageResource(R.drawable.ic_heart_red);
                mealDetailsPresenter.addMealToFav(singleMealItem);
            }else {
                viewDataBinding.icMealDetailsHeart.setImageResource(R.drawable.ic_heart_gray);
                mealDetailsPresenter.deleteMealFromFav(singleMealItem);
                Utils.showCustomSnackbar(requireView(),"Removed!");
            }

        });

        //add meal to calender
        viewDataBinding.icMealDetailsCalender.setOnClickListener(view -> {
            showDatePicker();
            clearCalenderTime();
            mealDetailsPresenter.addMealToPlanned(new PlannedMeal(calendar.getTime(),singleMealItem));
        });

    }

    private void clearCalenderTime() {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    private void showDatePicker() {

        calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
            requireContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.YEAR,year);
                    Utils.showCustomSnackbar(requireView(),"Added to calender");
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePicker.show();
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Toast.makeText(requireContext(), "error meal details", Toast.LENGTH_SHORT).show();
    }



}