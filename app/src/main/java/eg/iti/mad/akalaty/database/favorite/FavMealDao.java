package eg.iti.mad.akalaty.database.favorite;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface FavMealDao {

    @Query("select * from meals_table")
    Flowable<List<SingleMealItem>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(SingleMealItem singleMealItem);

    @Delete
    Completable deleteMeal(SingleMealItem singleMealItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<SingleMealItem> meals);

    @Query("DELETE FROM meals_table")
    Completable deleteAll();

}
