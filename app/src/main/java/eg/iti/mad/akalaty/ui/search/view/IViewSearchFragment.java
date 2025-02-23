package eg.iti.mad.akalaty.ui.search.view;

import java.util.List;

import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.RandomMealsItem;

public interface IViewSearchFragment {

    void showAllCategories(List<CategoriesItem> categoriesItemList);

    void showAllAreas(List<AreasItem> areasItemList);

    void showAllIngredients(List<IngredientsItem> ingredientsItemList);


    void showErrorMsg(String errorMsg);

}
