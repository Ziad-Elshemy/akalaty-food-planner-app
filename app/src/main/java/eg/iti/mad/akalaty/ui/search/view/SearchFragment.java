package eg.iti.mad.akalaty.ui.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentSearchBinding;
import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.view.area.OnAreaClickListener;
import eg.iti.mad.akalaty.ui.home.view.category.OnCategoryClickListener;
import eg.iti.mad.akalaty.ui.home.view.ingredient.OnIngredientClickListener;
import eg.iti.mad.akalaty.ui.search.presenter.SearchPresenter;


public class SearchFragment extends Fragment implements IViewSearchFragment, OnCategoryClickListener, OnIngredientClickListener, OnAreaClickListener {

    FragmentSearchBinding viewDataBinding;
    SearchPresenter presenter;
    AllCategoriesSearchAdapter allCategoriesSearchAdapter;
    AllIngredientsSearchAdapter allIngredientsSearchAdapter;
    AllAreasSearchAdapter allAreasSearchAdapter;


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
        setUpChoiceChip();
        allCategoriesSearchAdapter = new AllCategoriesSearchAdapter(requireContext(),new ArrayList<>(),this);
        allIngredientsSearchAdapter = new AllIngredientsSearchAdapter(requireContext(),new ArrayList<>(),this);
        allAreasSearchAdapter = new AllAreasSearchAdapter(requireContext(),new ArrayList<>(),this);

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

//    List<PhoneData> search(String name){
//        List<PhoneData> newList = new ArrayList<>();
//        for (int i=0;i<input.size();i++){
//            if (input.get(i).getName().equals(name)){
//                newList.add(input.get(i));
//            }
//        }
//        return newList;
//    }

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
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public void onCategoryItemClicked(CategoriesItem categoriesItem) {

    }

    @Override
    public void onIngredientItemClicked(IngredientsItem ingredientsItem) {

    }

    @Override
    public void onAreaItemClicked(AreasItem areasItem) {

    }
}