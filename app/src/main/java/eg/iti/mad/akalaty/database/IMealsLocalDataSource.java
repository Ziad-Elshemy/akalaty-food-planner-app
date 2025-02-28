package eg.iti.mad.akalaty.database;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface IMealsLocalDataSource {

    //favorite
    public Completable insertMeal(SingleMealItem singleMealItem);
    public Completable deleteMeal(SingleMealItem singleMealItem);
    public Flowable<List<SingleMealItem>> getAllMeals();
    public Completable insertAll(List<SingleMealItem> singleMealItem);

    public  Completable deleteAll();

    //planned
    public Completable insertPlannedMeal(PlannedMeal plannedMeal);
    public Completable deletePlannedMeal(PlannedMeal plannedMeal);
    public Flowable<List<PlannedMeal>> getAllPlannedMeals();
    public Flowable<List<PlannedMeal>> getMealByDate(Date date);
}
