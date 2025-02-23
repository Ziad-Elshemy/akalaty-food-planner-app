package eg.iti.mad.akalaty.database;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IMealsLocalDataSource {
    public void insertMeal(SingleMealItem singleMealItem);
    public void deleteMeal(SingleMealItem singleMealItem);
    public LiveData<List<SingleMealItem>> getAllMeals();
}
