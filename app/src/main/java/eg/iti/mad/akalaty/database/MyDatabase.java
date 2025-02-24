package eg.iti.mad.akalaty.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import eg.iti.mad.akalaty.database.favorite.FavMealDao;
import eg.iti.mad.akalaty.database.planned.DateConverter;
import eg.iti.mad.akalaty.database.planned.MealConverter;
import eg.iti.mad.akalaty.database.planned.PlannedMealDao;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;

//@TypeConverters(Converters.class)
@Database(entities = {SingleMealItem.class, PlannedMeal.class},version = 1)
@TypeConverters({DateConverter.class, MealConverter.class})
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance = null;

    public abstract FavMealDao getMealDao();
    public abstract PlannedMealDao getPlannedMealDao();

    public static MyDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class,"meals_db").build();
        }
        return instance;
    }






}
