package eg.iti.mad.akalaty.database;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IMealsLocalDataSource {

    //favorite
    public void insertMeal(SingleMealItem singleMealItem);
    public void deleteMeal(SingleMealItem singleMealItem);
    public LiveData<List<SingleMealItem>> getAllMeals();

    //planned
    public void insertPlannedMeal(PlannedMeal plannedMeal);
    public void deletePlannedMeal(PlannedMeal plannedMeal);
    public LiveData<List<PlannedMeal>> getAllPlannedMeals();
    public LiveData<List<PlannedMeal>> getMealByDate(Date date);
}
