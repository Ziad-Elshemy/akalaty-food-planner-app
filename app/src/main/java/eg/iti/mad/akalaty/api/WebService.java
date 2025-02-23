package eg.iti.mad.akalaty.api;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsItem;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.RandomMealsItem;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {

    @GET("categories.php")
    Call<CategoriesResponse> getAllCategories();
    @GET("list.php")
    Call<AreasResponse> getAllAreas(@Query("a") String areaId);
    @GET("list.php")
    Call<IngredientsResponse> getAllIngredients(@Query("i") String ingredientId);

    @GET("lookup.php")
    Call<SingleMealByIdResponse> getMealDetailsById(@Query("i") String mealId);

    @GET("search.php")
    Call<MealDetailsResponse> searchMealsByName(@Query("s") String mealName);

    @GET("random.php")
    Call<RandomMealResponse> getRandomMealDetails();

    @GET("filter.php")
    Call<FilteredMealsResponse> getMealsByCategory(@Query("c") String catId);
    @GET("filter.php")
    Call<FilteredMealsResponse> getMealsByArea(@Query("a") String areaId);
    @GET("filter.php")
    Call<FilteredMealsResponse> getMealsByIngredient(@Query("i") String ingredientId);
}
