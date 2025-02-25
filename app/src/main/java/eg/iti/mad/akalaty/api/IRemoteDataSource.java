package eg.iti.mad.akalaty.api;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import io.reactivex.rxjava3.core.Single;

public interface IRemoteDataSource {
    public Single<RandomMealResponse> getRandomMeal();
    public Single<CategoriesResponse> getAllCategories();
    public Single<FilteredMealsResponse> getMealsByCategoryId(String catId);
    public Single<AreasResponse> getAllAreas();
    public Single<FilteredMealsResponse> getMealsByAreaId( String areaId);
    public Single<IngredientsResponse> getAllIngredients();
    public Single<FilteredMealsResponse> getMealsByIngredientId( String ingredientId);
    public Single<MealDetailsResponse> searchMealsByName( String mealName);
    public Single<SingleMealByIdResponse> getMealById( String mealId);
}
