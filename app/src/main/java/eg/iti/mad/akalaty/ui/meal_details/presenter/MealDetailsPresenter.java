package eg.iti.mad.akalaty.ui.meal_details.presenter;

import android.util.Log;

import eg.iti.mad.akalaty.api.NetworkCallbackMealById;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.meal_details.view.IViewMealDetailsFragment;

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
        _repo.insertMealToFav(singleMealItem);
    }

    @Override
    public void deleteMealFromFav(SingleMealItem singleMealItem) {
        _repo.deleteMealFromFav(singleMealItem);
    }

    @Override
    public void addMealToPlanned(PlannedMeal plannedMeal) {
        _repo.insertMealToPlanned(plannedMeal);
    }

    @Override
    public void deleteMealFromPlanned(PlannedMeal plannedMeal) {
        _repo.deleteMealFromPlanned(plannedMeal);
    }

}
