package eg.iti.mad.akalaty.ui.search.presenter;

import java.util.List;

import eg.iti.mad.akalaty.api.NetworkCallbackAllAreas;
import eg.iti.mad.akalaty.api.NetworkCallbackAllCategories;
import eg.iti.mad.akalaty.api.NetworkCallbackAllIngredients;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByArea;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByCategory;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByIngredient;
import eg.iti.mad.akalaty.api.NetworkCallbackRandom;
import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.RandomMealsItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.presenter.IHomePresenter;
import eg.iti.mad.akalaty.ui.home.view.IViewHomeFragment;
import eg.iti.mad.akalaty.ui.search.view.IViewSearchFragment;

public class SearchPresenter implements ISearchPresenter, NetworkCallbackAllCategories,
        NetworkCallbackAllAreas, NetworkCallbackAllIngredients {

    IViewSearchFragment _view;
    MealsRepo _repo;

    public SearchPresenter(IViewSearchFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void getAllCategories() {
        _repo.getAllCategories(this);
    }


    @Override
    public void getAllAreas() {
        _repo.getAllAreas(this);
    }


    @Override
    public void getAllIngredient() {
        _repo.getAllIngredients(this);
    }


    @Override
    public void onSuccessAllCategoriesResult(List<CategoriesItem> categoriesItemList) {
        _view.showAllCategories(categoriesItemList);
    }

    @Override
    public void onFailureAllCategoriesResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
    }

    @Override
    public void onSuccessAllAreasResult(List<AreasItem> areasItems) {
        _view.showAllAreas(areasItems);
    }

    @Override
    public void onFailureAllAreasResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
    }

    @Override
    public void onSuccessAllIngredientsResult(List<IngredientsItem> ingredientsItems) {
        _view.showAllIngredients(ingredientsItems);
    }

    @Override
    public void onFailureAllIngredientsResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
    }

}
