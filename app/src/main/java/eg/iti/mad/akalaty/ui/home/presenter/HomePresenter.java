package eg.iti.mad.akalaty.ui.home.presenter;

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
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.view.IViewHomeFragment;

public class HomePresenter implements IHomePresenter , NetworkCallbackRandom, NetworkCallbackAllCategories, NetworkCallbackMealsByCategory ,
        NetworkCallbackAllAreas, NetworkCallbackMealsByArea , NetworkCallbackAllIngredients, NetworkCallbackMealsByIngredient {

    IViewHomeFragment _view;
    MealsRepo _repo;

    public HomePresenter(IViewHomeFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void getRandomMeal() {
        _repo.getRandomMeal(this);
    }

    @Override
    public void getAllCategories() {
        _repo.getAllCategories(this);
    }

    @Override
    public void getMealsByCategory(String catId) {
        _repo.getMealsByCategory(this,catId);
    }

    @Override
    public void getAllAreas() {
        _repo.getAllAreas(this);
    }

    @Override
    public void getMealsByArea(String areaId) {
        _repo.getMealsByArea(this,areaId);
    }

    @Override
    public void getAllIngredient() {
        _repo.getAllIngredients(this);
    }

    @Override
    public void getMealsByIngredient(String ingredientId) {
        _repo.getMealsByIngredient(this,ingredientId);
    }


    @Override
    public void onSuccessRandomMealResult(RandomMealsItem randomMeal) {
        _view.showRandomMeal(randomMeal);
    }

    @Override
    public void onFailureRandomMealResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
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
    public void onSuccessMealsByCategoryResult(List<FilteredMealsItem> filteredMealsItems) {
        _view.showMealsByCategory(filteredMealsItems);
    }

    @Override
    public void onFailureMealsByCategoryResult(String errorMsg) {
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
    public void onSuccessMealsByAreaResult(List<FilteredMealsItem> filteredMealsItems) {
        _view.showMealsByArea(filteredMealsItems);
    }

    @Override
    public void onFailureMealsByAreaResult(String errorMsg) {
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

    @Override
    public void onSuccessMealsByIngredientResult(List<FilteredMealsItem> filteredMealsItems) {
        _view.showMealsByIngredient(filteredMealsItems);
    }

    @Override
    public void onFailureMealsByIngredientResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
    }
}
