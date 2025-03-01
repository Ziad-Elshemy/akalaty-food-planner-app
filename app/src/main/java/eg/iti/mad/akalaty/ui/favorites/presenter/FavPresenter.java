package eg.iti.mad.akalaty.ui.favorites.presenter;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.favorites.view.IViewMyFavFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavPresenter implements IFavPresenter {

    private IViewMyFavFragment _view;
    private MealsRepo _repo;
    private static final String TAG = "FavPresenter";
    private final CompositeDisposable disposables = new CompositeDisposable();

    public FavPresenter(IViewMyFavFragment _view, MealsRepo _repo) {
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void getStoredMeals() {
        Flowable<List<SingleMealItem>> productList = _repo.getAllStoredFavMeals();
        productList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showFavMeals(list);
                        }
                );
    }

    @Override
    public void deleteFromFav(SingleMealItem singleMealItem) {
        disposables.add(
                _repo.deleteMealFromFav(singleMealItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            _view.showOnDeleteSuccess("Meal removed from favorites"); // تحديث الواجهة
                            Log.i(TAG, "Meal deleted from Room successfully");
                        }, throwable -> {
                            _view.showOnDeleteFailure("Error deleting meal from favorites");
                            Log.i(TAG, "Error deleting meal from Room", throwable);
                        })
        );
    }
}
