package eg.iti.mad.akalaty.ui.home.view;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

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


public class HomeFragment extends Fragment implements IViewHomeFragment , OnCategoryClickListener, OnMealClickListener, OnAreaClickListener, OnIngredientClickListener {


    FragmentHomeBinding viewDataBinding;
    HomePresenter homePresenter;

    AllCategoriesAdapter allCategoriesAdapter;
    MealsByCategoryAdapter mealsByCategoryAdapter;
    AllAreasAdapter allAreasAdapter;
    MealsByAreaAdapter mealsByAreaAdapter;
    AllIngredientsAdapter allIngredientsAdapter;
    MealsByIngredientAdapter mealsByIngredientAdapter;

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

        allCategoriesAdapter = new AllCategoriesAdapter(getContext(),new ArrayList<>(),this);
        mealsByCategoryAdapter = new MealsByCategoryAdapter(getContext(),new ArrayList<>(),this);
        allAreasAdapter = new AllAreasAdapter(getContext(),new ArrayList<>(),this);
        mealsByAreaAdapter = new MealsByAreaAdapter(getContext(),new ArrayList<>(),this);
        allIngredientsAdapter = new AllIngredientsAdapter(getContext(),new ArrayList<>(),this);
        mealsByIngredientAdapter = new MealsByIngredientAdapter(getContext(),new ArrayList<>(),this);

        viewDataBinding.recyclerViewCatList.setAdapter(allCategoriesAdapter);
        viewDataBinding.recyclerViewMealsByCat.setAdapter(mealsByCategoryAdapter);
        viewDataBinding.recyclerViewAreaList.setAdapter(allAreasAdapter);
        viewDataBinding.recyclerViewMealsByArea.setAdapter(mealsByAreaAdapter);
        viewDataBinding.recyclerViewIngredientList.setAdapter(allIngredientsAdapter);
        viewDataBinding.recyclerViewMealsByIngredient.setAdapter(mealsByIngredientAdapter);

        homePresenter = new HomePresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));

        homePresenter.getRandomMeal();
        homePresenter.getAllCategories();
        homePresenter.getMealsByCategory("Beef");
        homePresenter.getAllAreas();
        homePresenter.getMealsByArea("American");
        homePresenter.getAllIngredient();
        homePresenter.getMealsByIngredient("chicken_breast");


        viewDataBinding.imgMealDetails.setOnClickListener(view1 -> {


        });

//        //test getAllCategories api
//        RemoteDataSource.getApis().getAllCategories().enqueue(new Callback<CategoriesResponse>() {
//            @Override
//            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
//                Toast.makeText(getContext(), "getAllCategories: onResponse", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<CategoriesResponse> call, Throwable throwable) {
//                Toast.makeText(getContext(), "getAllCategories: onFailure", Toast.LENGTH_SHORT).show();
//            }
//        });


//        //test getMealDetails api
//        RemoteDataSource.getApis().getMealDetails("52980").enqueue(new Callback<MealDetailsResponse>() {
//            @Override
//            public void onResponse(Call<MealDetailsResponse> call, Response<MealDetailsResponse> response) {
//                Toast.makeText(getContext(), "getMealDetails: onResponse", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<MealDetailsResponse> call, Throwable throwable) {
//                Toast.makeText(getContext(), "getMealDetails: onFailure", Toast.LENGTH_SHORT).show();
//            }
//        });

//        //test getMealDetails api
//        RemoteDataSource.getInstance().getRandomMeal(this);





    }


    @Override
    public void showRandomMeal(RandomMealsItem randomMealsItem) {
        Toast.makeText(getContext(), "getRandomMealDetails onResponse", Toast.LENGTH_SHORT).show();
        Glide.with(requireActivity()).load(randomMealsItem.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(viewDataBinding.imgRandomMeal);
        viewDataBinding.imgMealArea.setImageResource(AreasImages.getAreaByName(randomMealsItem.getStrArea()));
        viewDataBinding.txtMealName.setText(randomMealsItem.getStrMeal());
        viewDataBinding.txtMealCat.setText(randomMealsItem.getStrCategory());
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
        Toast.makeText(requireContext(), ""+filteredMealsItem.getStrMeal(), Toast.LENGTH_SHORT).show();
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(filteredMealsItem.getIdMeal());
        Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
//        Navigation.findNavController(this).navigate(R.id);
    }

    @Override
    public void onAreaItemClicked(AreasItem areasItem) {
        homePresenter.getMealsByArea(areasItem.getStrArea());
    }


    @Override
    public void onIngredientItemClicked(IngredientsItem ingredientsItem) {
        homePresenter.getMealsByIngredient(ingredientsItem.getStrIngredient());
    }
}