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
import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface PlannedMealDao {

    @Query("select * from planned_meals_table")
    Flowable<List<PlannedMeal>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(PlannedMeal plannedMeal);

    @Delete
    Completable deleteMeal(PlannedMeal plannedMeal);

    @Query("select * from planned_meals_table where date = :date")
    Flowable<List<PlannedMeal>> getMealsByDate(Date date);

    @Query("select * from planned_meals_table where id = :id LIMIT 1")
    Single<PlannedMeal> getMealById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<PlannedMeal> meals);

    @Query("DELETE FROM planned_meals_table")
    Completable deleteAll();

}
