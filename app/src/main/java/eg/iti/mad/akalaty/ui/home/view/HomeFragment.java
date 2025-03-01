package eg.iti.mad.akalaty.ui.home.view;

import static androidx.navigation.Navigation.findNavController;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentHomeBinding;
import eg.iti.mad.akalaty.model.AreasImages;
import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.RandomMealsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.presenter.HomePresenter;
import eg.iti.mad.akalaty.ui.home.view.area.AllAreasAdapter;
import eg.iti.mad.akalaty.ui.home.view.area.MealsByAreaAdapter;
import eg.iti.mad.akalaty.ui.home.view.area.OnAreaClickListener;
import eg.iti.mad.akalaty.ui.home.view.category.AllCategoriesAdapter;
import eg.iti.mad.akalaty.ui.home.view.category.MealsByCategoryAdapter;
import eg.iti.mad.akalaty.ui.home.view.category.OnCategoryClickListener;
import eg.iti.mad.akalaty.ui.home.view.ingredient.AllIngredientsAdapter;
import eg.iti.mad.akalaty.ui.home.view.ingredient.MealsByIngredientAdapter;
import eg.iti.mad.akalaty.ui.home.view.ingredient.OnIngredientClickListener;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import eg.iti.mad.akalaty.utils.SharedPref;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomeFragment extends Fragment implements IViewHomeFragment , OnCategoryClickListener, OnMealClickListener, OnAreaClickListener, OnIngredientClickListener {

    private static final String TAG = "HomeFragment";
    FragmentHomeBinding viewDataBinding;
    HomePresenter homePresenter;
    Calendar calendar;
    AllCategoriesAdapter allCategoriesAdapter;
    MealsByCategoryAdapter mealsByCategoryAdapter;
    AllAreasAdapter allAreasAdapter;
    MealsByAreaAdapter mealsByAreaAdapter;
    AllIngredientsAdapter allIngredientsAdapter;
    MealsByIngredientAdapter mealsByIngredientAdapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeInternetStatus();

        setAdapters();

        homePresenter = new HomePresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));

    }

    private void setAdapters() {

        //init
        allCategoriesAdapter = new AllCategoriesAdapter(getContext(),new ArrayList<>(),this);
        mealsByCategoryAdapter = new MealsByCategoryAdapter(getContext(),new ArrayList<>(),this);
        allAreasAdapter = new AllAreasAdapter(getContext(),new ArrayList<>(),this);
        mealsByAreaAdapter = new MealsByAreaAdapter(getContext(),new ArrayList<>(),this);
        allIngredientsAdapter = new AllIngredientsAdapter(getContext(),new ArrayList<>(),this);
        mealsByIngredientAdapter = new MealsByIngredientAdapter(getContext(),new ArrayList<>(),this);
        //set
        viewDataBinding.recyclerViewCatList.setAdapter(allCategoriesAdapter);
        viewDataBinding.recyclerViewMealsByCat.setAdapter(mealsByCategoryAdapter);
        viewDataBinding.recyclerViewAreaList.setAdapter(allAreasAdapter);
        viewDataBinding.recyclerViewMealsByArea.setAdapter(mealsByAreaAdapter);
        viewDataBinding.recyclerViewIngredientList.setAdapter(allIngredientsAdapter);
        viewDataBinding.recyclerViewMealsByIngredient.setAdapter(mealsByIngredientAdapter);
    }

    private void setRandomMeal() {
        calendar = Calendar.getInstance();
        clearCalenderTime();
        Log.i(TAG, "onViewCreated: calendar"+calendar.getTime().getTime());
        Log.i(TAG, "onViewCreated: shared"+SharedPref.getInstance(requireActivity()).getMealDate());

        if (calendar.getTime().getTime()==SharedPref.getInstance(requireActivity()).getMealDate()){
            homePresenter.getMealById(SharedPref.getInstance(requireActivity()).getMealId());
            Log.i(TAG, "onViewCreated: true");
        }else {
            homePresenter.getRandomMeal();
            Log.i(TAG, "onViewCreated: false");
        }
        Log.i(TAG, "onViewCreated: calendar"+calendar.getTime().getTime());
        Log.i(TAG, "onViewCreated: shared"+SharedPref.getInstance(requireActivity()).getMealDate());
    }

    private void observeInternetStatus() {
        disposables.add(
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .map(tick -> NetworkUtils.isInternetAvailable(requireContext()))
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUi)
        );
    }
    private void updateUi(boolean isConnected) {
        if (isConnected) {
            viewDataBinding.imgNoInternet.setVisibility(View.GONE);
            viewDataBinding.homeScene.setVisibility(View.VISIBLE);
            setRandomMeal();
            homePresenter.getAllCategories();
            homePresenter.getMealsByCategory("Beef");
            homePresenter.getAllAreas();
            homePresenter.getMealsByArea("American");
            homePresenter.getAllIngredient();
            homePresenter.getMealsByIngredient("chicken_breast");
        } else {
            viewDataBinding.imgNoInternet.setVisibility(View.VISIBLE);
            viewDataBinding.homeScene.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }


    @Override
    public void showRandomMeal(RandomMealsItem randomMealsItem) {
        SharedPref.getInstance(requireActivity()).setMealId(randomMealsItem.getIdMeal());
        SharedPref.getInstance(requireActivity()).setMealDate(calendar.getTime().getTime());

        viewDataBinding.imgRandomMeal.setVisibility(View.VISIBLE);

        Glide.with(requireActivity()).load(randomMealsItem.getStrMealThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        viewDataBinding.lottieAnimationView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        viewDataBinding.lottieAnimationView.setVisibility(View.GONE);

                        Animation scaleFromCenter = AnimationUtils.loadAnimation(requireActivity(), R.anim.scale_up_from_center);
                        viewDataBinding.imgRandomMeal.startAnimation(scaleFromCenter);

                        return false;
                    }
                })
                .error(R.drawable.ic_logo)
                .into(viewDataBinding.imgRandomMeal);

        viewDataBinding.imgMealDetails.setOnClickListener(view -> {
            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(randomMealsItem.getIdMeal());
            Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
        });

        viewDataBinding.imgMealArea.setImageResource(AreasImages.getAreaByName(randomMealsItem.getStrArea()));
        viewDataBinding.txtMealName.setText(randomMealsItem.getStrMeal());
        viewDataBinding.txtMealCat.setText(randomMealsItem.getStrCategory());
    }


    @Override
    public void showMealById(SingleMealItem singleMealItem) {
        viewDataBinding.imgRandomMeal.setOnClickListener(view -> {
            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(singleMealItem.getIdMeal());
            Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
        });

        viewDataBinding.imgMealDetails.setOnClickListener(view -> {
            HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action =
                    HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(singleMealItem.getIdMeal());
            Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
        });

        viewDataBinding.lottieAnimationView.setVisibility(View.VISIBLE);

        Glide.with(requireActivity()).load(singleMealItem.getStrMealThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        viewDataBinding.lottieAnimationView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        viewDataBinding.lottieAnimationView.setVisibility(View.GONE);

                        Animation scaleFromCenter = AnimationUtils.loadAnimation(requireActivity(), R.anim.scale_up_from_center);
                        viewDataBinding.imgRandomMeal.startAnimation(scaleFromCenter);

                        return false;
                    }
                })
                .error(R.drawable.ic_logo)
                .into(viewDataBinding.imgRandomMeal);

        viewDataBinding.imgMealArea.setImageResource(AreasImages.getAreaByName(singleMealItem.getStrArea()));
        viewDataBinding.txtMealName.setText(singleMealItem.getStrMeal());
        viewDataBinding.txtMealCat.setText(singleMealItem.getStrCategory());
    }


    @Override
    public void showAllCategories(List<CategoriesItem> categoriesItemList) {
        allCategoriesAdapter.changeData(categoriesItemList);
    }

    @Override
    public void showMealsByCategory(List<FilteredMealsItem> filteredMealsItems) {
        mealsByCategoryAdapter.changeData(filteredMealsItems);
    }

    @Override
    public void showAllAreas(List<AreasItem> areasItemList) {
        allAreasAdapter.changeData(areasItemList);
    }

    @Override
    public void showMealsByArea(List<FilteredMealsItem> filteredMealsItems) {
        mealsByAreaAdapter.changeData(filteredMealsItems);
    }

    @Override
    public void showAllIngredients(List<IngredientsItem> ingredientsItemList) {
        allIngredientsAdapter.changeData(ingredientsItemList);
    }

    @Override
    public void showMealsByIngredient(List<FilteredMealsItem> filteredMealsItems) {
        mealsByIngredientAdapter.changeData(filteredMealsItems);
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Toast.makeText(getContext(), "getRandomMealDetails onError", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryItemClicked(CategoriesItem categoriesItem) {
        homePresenter.getMealsByCategory(categoriesItem.getStrCategory());
    }

    @Override
    public void onMealItemClicked(FilteredMealsItem filteredMealsItem) {
//        Toast.makeText(requireContext(), ""+filteredMealsItem.getStrMeal(), Toast.LENGTH_SHORT).show();
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(filteredMealsItem.getIdMeal());
        Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
    }

    @Override
    public void onAreaItemClicked(AreasItem areasItem) {
        homePresenter.getMealsByArea(areasItem.getStrArea());
    }


    @Override
    public void onIngredientItemClicked(IngredientsItem ingredientsItem) {
        homePresenter.getMealsByIngredient(ingredientsItem.getStrIngredient());
    }

    private void clearCalenderTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

}