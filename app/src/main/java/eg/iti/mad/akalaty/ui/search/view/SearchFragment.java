package eg.iti.mad.akalaty.ui.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentSearchBinding;
import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.view.HomeFragmentDirections;
import eg.iti.mad.akalaty.ui.home.view.OnMealClickListener;
import eg.iti.mad.akalaty.ui.home.view.area.OnAreaClickListener;
import eg.iti.mad.akalaty.ui.home.view.category.OnCategoryClickListener;
import eg.iti.mad.akalaty.ui.home.view.ingredient.OnIngredientClickListener;
import eg.iti.mad.akalaty.ui.search.presenter.SearchPresenter;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchFragment extends Fragment implements IViewSearchFragment,
        OnCategoryClickListener, OnIngredientClickListener, OnAreaClickListener, OnMealClickListener {

    FragmentSearchBinding viewDataBinding;
    SearchPresenter presenter;
    AllCategoriesSearchAdapter allCategoriesSearchAdapter;
    AllIngredientsSearchAdapter allIngredientsSearchAdapter;
    AllAreasSearchAdapter allAreasSearchAdapter;
    MealBySelectorSearchAdapter mealBySelectorSearchAdapter;

    private final CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false);

        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observeInternetStatus();

        setUpChoiceChip();
        allCategoriesSearchAdapter = new AllCategoriesSearchAdapter(requireContext(),new ArrayList<>(),this);
        allIngredientsSearchAdapter = new AllIngredientsSearchAdapter(requireContext(),new ArrayList<>(),this);
        allAreasSearchAdapter = new AllAreasSearchAdapter(requireContext(),new ArrayList<>(),this);
        mealBySelectorSearchAdapter = new MealBySelectorSearchAdapter(requireContext(),new ArrayList<>(),this);

        presenter = new SearchPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));

    }

    private void setUpChoiceChip(){
        for(int i=0;i<viewDataBinding.chipGroup.getChildCount();i++){
            Chip chip = (Chip) viewDataBinding.chipGroup.getChildAt(i);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked){
                        if (chip.getText().toString().equals("Category")){
//                            List<PhoneData> newList = search("Apple");
//                            myAdapter.changeData(newList);
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allCategoriesSearchAdapter);
                            presenter.getAllCategories();
                        }else if (chip.getText().toString().equals("Ingredient")){
//                            List<PhoneData> newList = search("Samsung");
//                            myAdapter.changeData(newList);
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allIngredientsSearchAdapter);
                            presenter.getAllIngredient();
                        }else if (chip.getText().toString().equals("Country")){
//                            List<PhoneData> newList = search("Oppo");
//                            myAdapter.changeData(newList);
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allAreasSearchAdapter);
                            presenter.getAllAreas();
                        }
                        Toast.makeText(requireContext(), chip.getText().toString(), Toast.LENGTH_SHORT).show();
                    }else {
//                        Toast.makeText(requireContext(), "Unchecked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
            viewDataBinding.noInternetScene.setVisibility(View.GONE);
            viewDataBinding.searchScene.setVisibility(View.VISIBLE);
        } else {
            viewDataBinding.noInternetScene.setVisibility(View.VISIBLE);
            viewDataBinding.searchScene.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }

    @Override
    public void showAllCategories(List<CategoriesItem> categoriesItemList) {
        allCategoriesSearchAdapter.changeData(categoriesItemList);
    }

    @Override
    public void showAllAreas(List<AreasItem> areasItemList) {
        allAreasSearchAdapter.changeData(areasItemList);
    }

    @Override
    public void showAllIngredients(List<IngredientsItem> ingredientsItemList) {
        allIngredientsSearchAdapter.changeData(ingredientsItemList);
    }

    @Override
    public void showMealsByCategory(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public void showMealsByArea(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
    }

    @Override
    public void showMealsByIngredient(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
    }

    @Override
    public void onCategoryItemClicked(CategoriesItem categoriesItem) {
        presenter.getMealsByCategory(categoriesItem.getStrCategory());
    }

    @Override
    public void onIngredientItemClicked(IngredientsItem ingredientsItem) {
        presenter.getMealsByIngredient(ingredientsItem.getStrIngredient());
    }

    @Override
    public void onAreaItemClicked(AreasItem areasItem) {
        Toast.makeText(requireContext(), ""+areasItem.getStrArea(), Toast.LENGTH_SHORT).show();
        presenter.getMealsByArea(areasItem.getStrArea());
    }

    @Override
    public void onMealItemClicked(FilteredMealsItem filteredMealsItem) {
        Toast.makeText(requireContext(), ""+filteredMealsItem.getStrMeal(), Toast.LENGTH_SHORT).show();
        SearchFragmentDirections.ActionSearchFragmentToMealDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToMealDetailsFragment(filteredMealsItem.getIdMeal());
        Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
    }
}