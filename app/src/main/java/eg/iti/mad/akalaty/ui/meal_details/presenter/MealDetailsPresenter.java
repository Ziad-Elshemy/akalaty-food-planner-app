package eg.iti.mad.akalaty.ui.meal_details.presenter;

import android.util.Log;

import java.util.List;

import eg.iti.mad.akalaty.api.NetworkCallbackAllAreas;
import eg.iti.mad.akalaty.api.NetworkCallbackAllCategories;
import eg.iti.mad.akalaty.api.NetworkCallbackAllIngredients;
import eg.iti.mad.akalaty.api.NetworkCallbackMealById;
import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.meal_details.view.IViewMealDetailsFragment;
import eg.iti.mad.akalaty.ui.search.presenter.ISearchPresenter;
import eg.iti.mad.akalaty.ui.search.view.IViewSearchFragment;

public class MealDetailsPresenter implements IMealDetailsPresenter, NetworkCallbackMealById {

    IViewMealDetailsFragment _view;
    MealsRepo _repo;

    public MealDetailsPresenter(IViewMealDetailsFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void onSuccessMealByIdResult(SingleMealItem singleMealItem) {
        _view.showMealDetails(singleMealItem);
    }

    @Override
    public void onFailureMealByIdResult(String errorMsg) {
        _view.showErrorMsg(errorMsg);
    }

    @Override
    public void getMailById(String mealId) {
        Log.i("testid", "getMailById: "+ mealId);
        _repo.getMealById(this,mealId);
    }


    @Override
    public void addMealToFav(SingleMealItem singleMealItem) {
        _repo.insertProduct(singleMealItem);
    }

}
