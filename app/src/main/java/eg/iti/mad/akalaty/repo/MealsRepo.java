package eg.iti.mad.akalaty.repo;

import eg.iti.mad.akalaty.api.NetworkCallbackAllAreas;
import eg.iti.mad.akalaty.api.NetworkCallbackAllCategories;
import eg.iti.mad.akalaty.api.NetworkCallbackAllIngredients;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByArea;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByCategory;
import eg.iti.mad.akalaty.api.NetworkCallbackMealsByIngredient;
import eg.iti.mad.akalaty.api.NetworkCallbackRandom;
import eg.iti.mad.akalaty.api.RemoteDataSource;

public class MealsRepo implements IMealsRepo{
    RemoteDataSource remoteDataSource;

    private static MealsRepo repo = null;

    private MealsRepo(RemoteDataSource remoteDataSource){
        this.remoteDataSource = remoteDataSource;
    }

    public static MealsRepo getInstance(RemoteDataSource remoteDataSource){
        if (repo==null){
            repo = new MealsRepo(remoteDataSource);
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
}
