package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.FilteredMealsItem;


public interface NetworkCallbackMealsByIngredient {
    public void onSuccessMealsByIngredientResult(List<FilteredMealsItem> filteredMealsItems);
    public void onFailureMealsByIngredientResult(String errorMsg);
}
