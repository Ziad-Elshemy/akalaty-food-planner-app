package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;


public interface NetworkCallbackMealsByCategory {
    public void onSuccessMealsByCategoryResult(List<FilteredMealsItem> filteredMealsItems);
    public void onFailureMealsByCategoryResult(String errorMsg);
}
