package eg.iti.mad.akalaty.api;

import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;


public interface NetworkCallbackMealById {
    public void onSuccessMealByIdResult(SingleMealItem singleMealItem);
    public void onFailureMealByIdResult(String errorMsg);
}
