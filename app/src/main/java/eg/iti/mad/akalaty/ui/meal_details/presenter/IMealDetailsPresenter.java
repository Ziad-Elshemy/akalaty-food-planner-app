package eg.iti.mad.akalaty.ui.meal_details.presenter;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IMealDetailsPresenter {
    void getMailById(String mealId);

    void addMealToFav(SingleMealItem singleMealItem);


}
