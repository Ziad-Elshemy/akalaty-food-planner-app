package eg.iti.mad.akalaty.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import eg.iti.mad.akalaty.model.SingleMealItem;

//@TypeConverters(Converters.class)
@Database(entities = {SingleMealItem.class},version = 1)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance = null;

    public abstract FavMealDao getMealDao();

    public static MyDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class,"meals_db").build();
        }
        return instance;
    }






}
