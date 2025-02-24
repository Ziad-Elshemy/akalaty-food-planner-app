package eg.iti.mad.akalaty.database.planned;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;


@Dao
public interface PlannedMealDao {

    @Query("select * from planned_meals_table")
    LiveData<List<PlannedMeal>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeal(PlannedMeal plannedMeal);

    @Delete
    void deleteMeal(PlannedMeal plannedMeal);

    @Query("select * from planned_meals_table where date = :date")
    LiveData<List<PlannedMeal>> getMealsByDate(Date date);

}
