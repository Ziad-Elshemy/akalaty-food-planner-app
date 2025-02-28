package eg.iti.mad.akalaty.ui.meal_details.presenter;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.meal_details.view.IViewMealDetailsFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements IMealDetailsPresenter{

    IViewMealDetailsFragment _view;
    MealsRepo _repo;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailsPresenter(IViewMealDetailsFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }

    @Override
    public void getMailById(String mealId) {
        Single<SingleMealByIdResponse> call = _repo.getMealById(mealId);
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getSingleMeal().get(0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            _view.showMealDetails(item);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }


    @Override
    public void addMealToFav(SingleMealItem singleMealItem) {
        _repo.insertMealToFav(singleMealItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void deleteMealFromFav(SingleMealItem singleMealItem) {
        _repo.deleteMealFromFav(singleMealItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void addMealToPlanned(PlannedMeal plannedMeal) {
        _repo.insertMealToPlanned(plannedMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void deleteMealFromPlanned(PlannedMeal plannedMeal) {
        _repo.deleteMealFromPlanned(plannedMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void clearDisposables() {
        disposables.clear();
    }

}
