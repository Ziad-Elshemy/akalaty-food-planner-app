package eg.iti.mad.akalaty.ui.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
import eg.iti.mad.akalaty.model.MealDetailsItem;
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
import io.reactivex.rxjava3.subjects.PublishSubject;


public class SearchFragment extends Fragment implements IViewSearchFragment,
        OnCategoryClickListener, OnIngredientClickListener, OnAreaClickListener, OnMealClickListener {

    private static final String TAG = "SearchFragment";
    FragmentSearchBinding viewDataBinding;
    SearchPresenter presenter;
    AllCategoriesSearchAdapter allCategoriesSearchAdapter;
    AllIngredientsSearchAdapter allIngredientsSearchAdapter;
    AllAreasSearchAdapter allAreasSearchAdapter;
    MealBySelectorSearchAdapter mealBySelectorSearchAdapter;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final CompositeDisposable searchDisposables = new CompositeDisposable();
    private PublishSubject<String> searchSubject = PublishSubject.create();


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



        allCategoriesSearchAdapter = new AllCategoriesSearchAdapter(requireContext(),new ArrayList<>(),this);
        allIngredientsSearchAdapter = new AllIngredientsSearchAdapter(requireContext(),new ArrayList<>(),this);
        allAreasSearchAdapter = new AllAreasSearchAdapter(requireContext(),new ArrayList<>(),this);
        mealBySelectorSearchAdapter = new MealBySelectorSearchAdapter(requireContext(),new ArrayList<>(),this);

        presenter = new SearchPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));

        setUpChoiceChip();


    }

    private void setUpChoiceChip(){
        viewDataBinding.chipGroup.check(R.id.chipCategory);
        viewDataBinding.recyclerViewSearchMain.setAdapter(allCategoriesSearchAdapter);
        presenter.getAllCategories();
        viewDataBinding.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, List<Integer> checkedIds) {
                if (checkedIds.isEmpty()) {
                    viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
                    setupSearchObservable();
                } else {
                    searchDisposables.clear();
                    int selectedChipId = checkedIds.get(0);
                    Chip selectedChip = group.findViewById(selectedChipId);

                    if (selectedChip != null) {
                        String selectedText = selectedChip.getText().toString();
                        if (selectedText.equals("Category")) {
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allCategoriesSearchAdapter);
                            presenter.getAllCategories();
                        } else if (selectedText.equals("Ingredient")) {
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allIngredientsSearchAdapter);
                            presenter.getAllIngredient();
                        } else if (selectedText.equals("Country")) {
                            viewDataBinding.recyclerViewSearchMain.setAdapter(allAreasSearchAdapter);
                            presenter.getAllAreas();
                        } else if (selectedText.equals("All")) {
                            viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
                            setupSearchObservable();
                        }
                    }
                }
            }
        });

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
        disposables.clear();
    }


    @Override
    public void showAllCategories(List<CategoriesItem> categoriesItemList) {
        allCategoriesSearchAdapter.changeData(categoriesItemList);
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(categoriesItemList)
                                .filter(name -> name.getStrCategory().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            allCategoriesSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showAllAreas(List<AreasItem> areasItemList) {
        allAreasSearchAdapter.changeData(areasItemList);
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(areasItemList)
                                .filter(name -> name.getStrArea().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            allAreasSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showAllIngredients(List<IngredientsItem> ingredientsItemList) {
        allIngredientsSearchAdapter.changeData(ingredientsItemList);
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(ingredientsItemList)
                                .filter(name -> name.getStrIngredient().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            allIngredientsSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showMealsByCategory(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
        searchDisposables.clear();
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(filteredMealsItems)
                                .filter(name -> name.getStrMeal().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            mealBySelectorSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public void showMealsByArea(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
        searchDisposables.clear();
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(filteredMealsItems)
                                .filter(name -> name.getStrMeal().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            mealBySelectorSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showMealsByIngredient(List<FilteredMealsItem> filteredMealsItems) {
        mealBySelectorSearchAdapter.changeData(filteredMealsItems);
        viewDataBinding.recyclerViewSearchMain.setAdapter(mealBySelectorSearchAdapter);
        searchDisposables.clear();
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .map(String::toLowerCase)
                        .switchMap(query -> Observable.fromIterable(filteredMealsItems)
                                .filter(name -> name.getStrMeal().toLowerCase().contains(query))
                                .toList()
                                .toObservable()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredList -> {
                            Log.i(TAG, "Filtered List: " + filteredList);
                            mealBySelectorSearchAdapter.changeData(filteredList);
                        })
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchSubject.onNext(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void showMealsBySearch(List<FilteredMealsItem> mealDetailsItems) {
        Log.i(TAG, "showMealsBySearch: "+mealDetailsItems);
        mealBySelectorSearchAdapter.changeData(mealDetailsItems);
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

    private void setupSearchObservable() {
        searchDisposables.clear();
        searchDisposables.add(
                searchSubject
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .distinctUntilChanged()
                        .subscribe(query -> presenter.getMealsBySearch(query))
        );

        viewDataBinding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}