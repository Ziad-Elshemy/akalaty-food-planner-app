package eg.iti.mad.akalaty.ui.search.view;

import java.util.List;

import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealsItem;

public interface IViewSearchFragment {

    void showAllCategories(List<CategoriesItem> categoriesItemList);

    void showAllAreas(List<AreasItem> areasItemList);

    void showAllIngredients(List<IngredientsItem> ingredientsItemList);


    void showMealsByCategory(List<FilteredMealsItem> filteredMealsItems);


    void showErrorMsg(String errorMsg);

    void showMealsByArea(List<FilteredMealsItem> filteredMealsItems);

    void showMealsByIngredient(List<FilteredMealsItem> filteredMealsItems);
    void showMealsBySearch(List<FilteredMealsItem> filteredMealsItems);
}
