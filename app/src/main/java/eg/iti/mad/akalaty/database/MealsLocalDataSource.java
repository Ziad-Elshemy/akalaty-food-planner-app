package eg.iti.mad.akalaty.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;


public class MealsLocalDataSource implements IMealsLocalDataSource{
    FavMealDao dao;
    private static MealsLocalDataSource localDataSource = null;
    private LiveData<List<SingleMealItem>> storedMeal;

    public MealsLocalDataSource(Context context){
        MyDatabase db = MyDatabase.getInstance(context);
        dao = db.getMealDao();
        storedMeal = dao.getAllMeals();
    }

    public static MealsLocalDataSource getInstance(Context context){
        if (localDataSource==null){
            localDataSource = new MealsLocalDataSource(context);
        }
        return localDataSource;
    }

//    public

    @Override
    public void insertMeal(SingleMealItem singleMealItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.insertMeal(singleMealItem);
            }
        }).start();
    }

    @Override
    public void deleteMeal(SingleMealItem singleMealItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao.deleteMeal(singleMealItem);
            }
        }).start();
    }

    @Override
    public LiveData<List<SingleMealItem>> getAllMeals(){
        return storedMeal;
    }
    


}
