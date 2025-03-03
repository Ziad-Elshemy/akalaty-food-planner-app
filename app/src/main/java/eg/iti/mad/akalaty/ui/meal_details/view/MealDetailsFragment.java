package eg.iti.mad.akalaty.ui.meal_details.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.auth.FirebaseDataSource;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import eg.iti.mad.akalaty.utils.SharedPref;
import eg.iti.mad.akalaty.utils.Utils;
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

    Dialog dialog;

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
        mealDetailsPresenter = new MealDetailsPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext()), new FirebaseDataSource()));
//        mealDetailsPresenter.getMailById(mealId);
        getMealDetails(mealId);


    }

    public void getMealDetails(String mealId) {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
             mealDetailsPresenter.getOnlineMealById(mealId);
        } else {
            mealDetailsPresenter.getFavMealById(mealId);
            mealDetailsPresenter.getPlannedMealById(mealId);
        }
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
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(viewDataBinding.imgMealDetailsImage);
        viewDataBinding.imgMealDetailsArea.setImageResource(AreasImages.getAreaByName(singleMealItem.getStrArea()));
//        Toast.makeText(requireContext(), "ziad success meal details", Toast.LENGTH_SHORT).show();

        mealDetailsIngredientsAdapter.changeData(createIngredientPairs(singleMealItem));
        List<String> instructionsList = Arrays.asList(singleMealItem.getStrInstructions().split("\r\n"));
        mealDetailsInstructionsAdapter.changeData(instructionsList);

        setupYoutubeVideo(singleMealItem);

        //add meal to database
        viewDataBinding.icMealDetailsHeart.setOnClickListener(view1 -> {
            if (!SharedPref.getInstance(requireContext()).getIsLogged()){
                showLoginDialog();
            }
            else {
                isFavorite = !isFavorite;
                if(isFavorite){
                    viewDataBinding.icMealDetailsHeart.setImageResource(R.drawable.ic_heart_red);
                    mealDetailsPresenter.addMealToFav(singleMealItem);
                    Utils.showCustomSnackbar(requireView(),getString(R.string.added_to_favorite));
                }else {
                    viewDataBinding.icMealDetailsHeart.setImageResource(R.drawable.ic_heart_gray);
                    mealDetailsPresenter.deleteMealFromFav(singleMealItem);
                    Utils.showCustomSnackbar(requireView(),getString(R.string.removed));
                }
            }


        });

        //add meal to calender
        viewDataBinding.icMealDetailsCalender.setOnClickListener(view -> {
            if (!SharedPref.getInstance(requireContext()).getIsLogged()){
                showLoginDialog();
            }
            else {
                showDatePicker(singleMealItem);
            }

        });

    }

    private void clearCalenderTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void showDatePicker(SingleMealItem singleMealItem) {

        calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
            requireContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.DAY_OF_MONTH,day);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.YEAR,year);
                    clearCalenderTime();
                    Log.i(TAG, "showMealDetails: calender "+calendar.getTime());
                    mealDetailsPresenter.addMealToPlanned(new PlannedMeal(calendar.getTime(),singleMealItem));
                    Utils.showCustomSnackbar(requireView(),getString(R.string.added_to_calender));
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

    private void showLoginDialog() {

        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText(R.string.please_login_first);
        Button login = dialog.findViewById(R.id.btnAction);
        login.setText(R.string.login);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        if (!errorMsg.equals("data not in table")){
            Utils.showCustomSnackbar(requireView(),errorMsg);
        }

    }



}