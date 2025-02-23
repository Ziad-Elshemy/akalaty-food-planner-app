package eg.iti.mad.akalaty.repo;

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
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IMealsRepo {

    //remote
    public void getRandomMeal(NetworkCallbackRandom networkCallbackRandom);

    public void getAllCategories(NetworkCallbackAllCategories networkCallbackAllCategories);
    public void getMealsByCategory(NetworkCallbackMealsByCategory networkCallbackMealsByCategory, String catId);

    public void getAllAreas(NetworkCallbackAllAreas networkCallbackAllAreas);
    public void getMealsByArea(NetworkCallbackMealsByArea networkCallbackMealsByArea, String areaId);

    public void getAllIngredients(NetworkCallbackAllIngredients networkCallbackAllIngredient);
    public void getMealsByIngredient(NetworkCallbackMealsByIngredient networkCallbackMealsByIngredient, String ingredientId);
    public void getMealById(NetworkCallbackMealById networkCallbackMealById, String mealId);

    //local
    public LiveData<List<SingleMealItem>> getAllStoredMeals();
    public void insertProduct(SingleMealItem singleMealItem);
    public void deleteProduct(SingleMealItem singleMealItem);

}
