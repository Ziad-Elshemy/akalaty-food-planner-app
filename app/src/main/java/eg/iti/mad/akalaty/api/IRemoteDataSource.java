package eg.iti.mad.akalaty.api;

public interface IRemoteDataSource {
    public void getRandomMeal(NetworkCallbackRandom networkCallbackRandom);
    public void getAllCategories(NetworkCallbackAllCategories networkCallbackAllCategories);
    public void getMealsByCategoryId(NetworkCallbackMealsByCategory networkCallbackMealsByCategory, String catId);
    public void getAllAreas(NetworkCallbackAllAreas networkCallbackAllAreas);
    public void getMealsByAreaId(NetworkCallbackMealsByArea networkCallbackMealsByArea, String areaId);
    public void getAllIngredients(NetworkCallbackAllIngredients networkCallbackAllIngredients);
    public void getMealsByIngredientId(NetworkCallbackMealsByIngredient networkCallbackMealsByIngredients, String ingredientId);
    public void searchMealsByName(NetworkCallbackSearchMealsByName networkCallbackSearchMealsByName, String mealName);
    public void getMealById(NetworkCallbackMealById networkCallbackMealById, String mealId);
}
