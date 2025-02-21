package eg.iti.mad.akalaty.repo;

import eg.iti.mad.akalaty.api.NetworkCallbackAllAreas;
import eg.iti.mad.akalaty.api.NetworkCallbackAllCategories;
import eg.iti.mad.akalaty.api.NetworkCallbackAllIngredients;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByArea;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByCategory;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByIngredient;
import eg.iti.mad.akalaty.api.NetworkCallbackRandom;

public interface IMealsRepo {
    public void getRandomMeal(NetworkCallbackRandom networkCallbackRandom);

    public void getAllCategories(NetworkCallbackAllCategories networkCallbackAllCategories);
    public void getMealsByCategory(NetworkCallbackMealsByCategory networkCallbackMealsByCategory, String catId);

    public void getAllAreas(NetworkCallbackAllAreas networkCallbackAllAreas);
    public void getMealsByArea(NetworkCallbackMealsByArea networkCallbackMealsByArea, String areaId);

    public void getAllIngredients(NetworkCallbackAllIngredients networkCallbackAllIngredient);
    public void getMealsByIngredient(NetworkCallbackMealsByIngredient networkCallbackMealsByIngredient, String ingredientId);

}
