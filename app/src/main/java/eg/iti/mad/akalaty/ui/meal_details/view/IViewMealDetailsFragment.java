package eg.iti.mad.akalaty.ui.meal_details.view;

import java.util.List;

import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;
import eg.iti.mad.akalaty.model.IngredientsItem;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IViewMealDetailsFragment {

    void showMealDetails(SingleMealItem singleMealItem);


    void showErrorMsg(String errorMsg);

}
