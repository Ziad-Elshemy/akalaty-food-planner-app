package eg.iti.mad.akalaty.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import eg.iti.mad.akalaty.api.NetworkCallbackAllAreas;
import eg.iti.mad.akalaty.api.NetworkCallbackAllCategories;
import eg.iti.mad.akalaty.api.NetworkCallbackAllIngredients;
import eg.iti.mad.akalaty.api.NetworkCallbackMealById;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByArea;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByCategory;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByIngredient;
import eg.iti.mad.akalaty.api.NetworkCallbackRandom;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;

public class MealsRepo implements IMealsRepo{
    RemoteDataSource remoteDataSource;
    MealsLocalDataSource mealsLocalDataSource;

    private static MealsRepo repo = null;

    private MealsRepo(RemoteDataSource remoteDataSource, MealsLocalDataSource mealsLocalDataSource){
        this.remoteDataSource = remoteDataSource;
        this.mealsLocalDataSource = mealsLocalDataSource;
    }

    public static MealsRepo getInstance(RemoteDataSource remoteDataSource, MealsLocalDataSource mealsLocalDataSource){
        if (repo==null){
            repo = new MealsRepo(remoteDataSource,mealsLocalDataSource);
        }
        return repo;
    }

    @Override
    public void getRandomMeal(NetworkCallbackRandom networkCallbackRandom) {
        remoteDataSource.getRandomMeal(networkCallbackRandom);
    }

    @Override
    public void getAllCategories(NetworkCallbackAllCategories networkCallbackAllCategories) {
        remoteDataSource.getAllCategories(networkCallbackAllCategories);
    }

    @Override
    public void getMealsByCategory(NetworkCallbackMealsByCategory networkCallbackMealsByCategory, String catId) {
        remoteDataSource.getMealsByCategoryId(networkCallbackMealsByCategory,catId);
    }

    @Override
    public void getAllAreas(NetworkCallbackAllAreas networkCallbackAllAreas) {
        remoteDataSource.getAllAreas(networkCallbackAllAreas);
    }

    @Override
    public void getMealsByArea(NetworkCallbackMealsByArea networkCallbackMealsByArea, String areaId) {
        remoteDataSource.getMealsByAreaId(networkCallbackMealsByArea, areaId);
    }

    @Override
    public void getAllIngredients(NetworkCallbackAllIngredients networkCallbackAllIngredient) {
        remoteDataSource.getAllIngredients(networkCallbackAllIngredient);
    }

    @Override
    public void getMealsByIngredient(NetworkCallbackMealsByIngredient networkCallbackMealsByIngredient, String ingredientId) {
        remoteDataSource.getMealsByIngredientId(networkCallbackMealsByIngredient,ingredientId);
    }

    @Override
    public void getMealById(NetworkCallbackMealById networkCallbackMealById, String mealId) {
        Log.i("testid", "getMealById: "+mealId);
        remoteDataSource.getMealById(networkCallbackMealById,mealId);
    }

    @Override
    public LiveData<List<SingleMealItem>> getAllStoredFavMeals() {
        return mealsLocalDataSource.getAllMeals();
    }

    @Override
    public void insertMealToFav(SingleMealItem singleMealItem) {
        mealsLocalDataSource.insertMeal(singleMealItem);
    }

    @Override
    public void deleteMealFromFav(SingleMealItem singleMealItem) {
        mealsLocalDataSource.deleteMeal(singleMealItem);
    }

    @Override
    public LiveData<List<PlannedMeal>> getAllStoredPlannedMeals() {
        return mealsLocalDataSource.getAllPlannedMeals();
    }

    @Override
    public void insertMealToPlanned(PlannedMeal plannedMeal) {
        mealsLocalDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public void deleteMealFromPlanned(PlannedMeal plannedMeal) {
        mealsLocalDataSource.deletePlannedMeal(plannedMeal);
    }
}
