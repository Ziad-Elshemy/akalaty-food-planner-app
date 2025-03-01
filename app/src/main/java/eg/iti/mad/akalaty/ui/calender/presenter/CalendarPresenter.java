package eg.iti.mad.akalaty.ui.calender.presenter;

import android.util.Log;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.calender.view.IViewCalendarFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPresenter implements ICalendarPresenter {

    private static final String TAG = "CalendarPresenter";
    private IViewCalendarFragment _view;
    private MealsRepo _repo;

    private final CompositeDisposable disposables = new CompositeDisposable();

    public CalendarPresenter(IViewCalendarFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void getPlannedMealsByDate(Date date) {
        Flowable<List<PlannedMeal>> productList = _repo.getPlannedMealsByDate(date);
        productList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            Log.i(TAG, "getPlannedMealsByDate: "+date);
                            _view.showPlannedMeals(list);
                        }
                );
    }

    @Override
    public void deleteFromCal(PlannedMeal plannedMeal) {
        disposables.add(
                _repo.deleteMealFromPlanned(plannedMeal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.i(TAG, "Meal deleted successfully: " + plannedMeal.getMeal().getStrMeal());
                            _view.showOnDeleteSuccess(" removed from planned meals");
                            getPlannedMealsByDate(plannedMeal.getDate());
                        }, throwable -> {
                            Log.e(TAG, "Error deleting meal: ", throwable);
                            _view.showOnDeleteFailure("Error deleting meal: " + throwable.getLocalizedMessage());
                        })
        );
    }
}
