package eg.iti.mad.akalaty.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.database.favorite.FavMealDao;
import eg.iti.mad.akalaty.database.planned.PlannedMealDao;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


public class MealsLocalDataSource implements IMealsLocalDataSource{
    FavMealDao favDao;
    PlannedMealDao plannedDao;
    private static MealsLocalDataSource localDataSource = null;
    private Flowable<List<SingleMealItem>> storedMeal;
    private Flowable<List<PlannedMeal>> storedPlannedMeal;

    public MealsLocalDataSource(Context context){
        MyDatabase db = MyDatabase.getInstance(context);
        favDao = db.getMealDao();
        storedMeal = favDao.getAllMeals();
        plannedDao = db.getPlannedMealDao();
        storedPlannedMeal = plannedDao.getAllMeals();
    }

    public static MealsLocalDataSource getInstance(Context context){
        if (localDataSource==null){
            localDataSource = new MealsLocalDataSource(context);
        }
        return localDataSource;
    }


    // fav meals
    @Override
    public Completable insertMeal(SingleMealItem singleMealItem){
        return favDao.insertMeal(singleMealItem);
    }

    @Override
    public Completable deleteMeal(SingleMealItem singleMealItem){
        return favDao.deleteMeal(singleMealItem);
    }

    @Override
    public Flowable<List<SingleMealItem>> getAllMeals(){
        return storedMeal;
    }

    @Override
    public Completable insertAll(List<SingleMealItem> singleMealItem) {
        return favDao.insertAll(singleMealItem);
    }

    @Override
    public Completable deleteAll() {
        return favDao.deleteAll();
    }


    //planned meals
    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return plannedDao.insertMeal(plannedMeal);
    }

    @Override
    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return plannedDao.deleteMeal(plannedMeal);
    }

    @Override
    public Flowable<List<PlannedMeal>> getAllPlannedMeals() {
        return storedPlannedMeal;
    }

    @Override
    public Flowable<List<PlannedMeal>> getMealByDate(Date date) {
        return plannedDao.getMealsByDate(date);
    }


}
