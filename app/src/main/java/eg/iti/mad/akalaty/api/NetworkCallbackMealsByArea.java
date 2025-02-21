package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.FilteredMealsItem;


public interface NetworkCallbackMealsByArea {
    public void onSuccessMealsByAreaResult(List<FilteredMealsItem> filteredMealsItems);
    public void onFailureMealsByAreaResult(String errorMsg);
}
