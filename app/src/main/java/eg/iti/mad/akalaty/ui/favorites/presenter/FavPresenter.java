package eg.iti.mad.akalaty.ui.favorites.presenter;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.favorites.view.IViewMyFavFragment;

public class FavPresenter implements IFavPresenter{

    private IViewMyFavFragment _view;
    private MealsRepo _repo;

    public FavPresenter(IViewMyFavFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public LiveData<List<SingleMealItem>> getStoredMeals() {
        return _repo.getAllStoredFavMeals();
    }

    @Override
    public void deleteFromFav(SingleMealItem singleMealItem) {
        _repo.deleteMealFromFav(singleMealItem);
    }
}
