package eg.iti.mad.akalaty.api;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {

    @GET("categories.php")
    Single<CategoriesResponse> getAllCategories();
    @GET("list.php")
    Single<AreasResponse> getAllAreas(@Query("a") String areaId);
    @GET("list.php")
    Single<IngredientsResponse> getAllIngredients(@Query("i") String ingredientId);

    @GET("lookup.php")
    Single<SingleMealByIdResponse> getMealDetailsById(@Query("i") String mealId);

    @GET("search.php")
    Single<FilteredMealsResponse> searchMealsByName(@Query("s") String mealName);

    @GET("random.php")
    Single<RandomMealResponse> getRandomMealDetails();

    @GET("filter.php")
    Single<FilteredMealsResponse> getMealsByCategory(@Query("c") String catId);
    @GET("filter.php")
    Single<FilteredMealsResponse> getMealsByArea(@Query("a") String areaId);
    @GET("filter.php")
    Single<FilteredMealsResponse> getMealsByIngredient(@Query("i") String ingredientId);
}
