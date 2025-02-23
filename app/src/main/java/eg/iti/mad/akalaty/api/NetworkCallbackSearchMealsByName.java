package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.MealDetailsResponse;


public interface NetworkCallbackSearchMealsByName {
    public void onSuccessSearchMealsByNameResult(List<MealDetailsItem> mealDetailsItemList);
    public void onFailureSearchMealsByNameResult(String errorMsg);
}
