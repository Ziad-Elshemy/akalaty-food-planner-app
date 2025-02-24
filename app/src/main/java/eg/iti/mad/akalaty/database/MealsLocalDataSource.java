package eg.iti.mad.akalaty.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.database.favorite.FavMealDao;
import eg.iti.mad.akalaty.database.planned.PlannedMealDao;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;


public class MealsLocalDataSource implements IMealsLocalDataSource{
    FavMealDao favDao;
    PlannedMealDao plannedDao;
    private static MealsLocalDataSource localDataSource = null;
    private LiveData<List<SingleMealItem>> storedMeal;
    private LiveData<List<PlannedMeal>> storedPlannedMeal;

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
    public void insertMeal(SingleMealItem singleMealItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                favDao.insertMeal(singleMealItem);
            }
        }).start();
    }

    @Override
    public void deleteMeal(SingleMealItem singleMealItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                favDao.deleteMeal(singleMealItem);
            }
        }).start();
    }

    @Override
    public LiveData<List<SingleMealItem>> getAllMeals(){
        return storedMeal;
    }



    //planned meals
    @Override
    public void insertPlannedMeal(PlannedMeal plannedMeal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedDao.insertMeal(plannedMeal);
            }
        }).start();
    }

    @Override
    public void deletePlannedMeal(PlannedMeal plannedMeal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                plannedDao.deleteMeal(plannedMeal);
            }
        }).start();
    }

    @Override
    public LiveData<List<PlannedMeal>> getAllPlannedMeals() {
        return storedPlannedMeal;
    }

    @Override
    public LiveData<List<PlannedMeal>> getMealByDate(Date date) {
        return plannedDao.getMealsByDate(date);
    }


}
