package eg.iti.mad.akalaty.ui.favorites.presenter;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IFavPresenter {

    public LiveData<List<SingleMealItem>> getStoredMeals();
    public void deleteFromFav(SingleMealItem singleMealItem);

}
