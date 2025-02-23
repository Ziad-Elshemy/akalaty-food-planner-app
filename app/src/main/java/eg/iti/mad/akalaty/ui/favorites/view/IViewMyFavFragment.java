package eg.iti.mad.akalaty.ui.favorites.view;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IViewMyFavFragment {
    public void showFavMeals(LiveData<List<SingleMealItem>> mealsList);
}
