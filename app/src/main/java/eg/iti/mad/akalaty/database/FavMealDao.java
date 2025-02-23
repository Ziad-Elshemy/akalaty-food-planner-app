package eg.iti.mad.akalaty.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;


@Dao
public interface FavMealDao {

    @Query("select * from meals_table")
    LiveData<List<SingleMealItem>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeal(SingleMealItem singleMealItem);

    @Delete
    void deleteMeal(SingleMealItem singleMealItem);

}
