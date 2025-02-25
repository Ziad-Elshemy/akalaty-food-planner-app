package eg.iti.mad.akalaty.ui.calender.presenter;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.calender.view.IViewCalendarFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPresenter implements ICalendarPresenter {

    private IViewCalendarFragment _view;
    private MealsRepo _repo;

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
                            _view.showPlannedMeals(list);
                        }
                );
    }

    @Override
    public void deleteFromCal(PlannedMeal plannedMeal) {
        _repo.deleteMealFromPlanned(plannedMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
