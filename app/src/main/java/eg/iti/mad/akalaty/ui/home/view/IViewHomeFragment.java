package eg.iti.mad.akalaty.ui.home.view;

import java.util.List;

import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.RandomMealsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IViewHomeFragment {
    void showRandomMeal(RandomMealsItem randomMealsItem);
    void showMealById(SingleMealItem singleMealItem);

    void showAllCategories(List<CategoriesItem> categoriesItemList);
    void showMealsByCategory(List<FilteredMealsItem> filteredMealsItems);

    void showAllAreas(List<AreasItem> areasItemList);
    void showMealsByArea(List<FilteredMealsItem> filteredMealsItems);

    void showAllIngredients(List<IngredientsItem> ingredientsItemList);
    void showMealsByIngredient(List<FilteredMealsItem> filteredMealsItems);


    void showErrorMsg(String errorMsg);

}
