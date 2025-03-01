package eg.iti.mad.akalaty.ui.meal_details.presenter;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IMealDetailsPresenter {
    void getOnlineMealById(String mealId);

    void addMealToFav(SingleMealItem singleMealItem);
    void deleteMealFromFav(SingleMealItem singleMealItem);
    void getFavMealById(String mealId);


    //planned
    void addMealToPlanned(PlannedMeal plannedMeal);
    void deleteMealFromPlanned(PlannedMeal plannedMeal);


}
