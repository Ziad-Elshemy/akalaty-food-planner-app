package eg.iti.mad.akalaty.database;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IMealsLocalDataSource {

    //favorite
    public Completable insertMeal(SingleMealItem singleMealItem);
    public Completable deleteMeal(SingleMealItem singleMealItem);
    public Flowable<List<SingleMealItem>> getAllMeals();
    public Completable insertAll(List<SingleMealItem> singleMealItem);
    public Single<SingleMealItem> getMealById(String mealId);
    public  Completable deleteAllFav();

    //planned
    public Completable insertPlannedMeal(PlannedMeal plannedMeal);
    public Completable deletePlannedMeal(PlannedMeal plannedMeal);
    public Flowable<List<PlannedMeal>> getAllPlannedMeals();
    public Flowable<List<PlannedMeal>> getMealByDate(Date date);
    public Single<PlannedMeal> getPlanedMealById(String mealId);
    public Completable insertAllPlanned(List<PlannedMeal> plannedMealList);
    public  Completable deleteAllPlanned();
}
