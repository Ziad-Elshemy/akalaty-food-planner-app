package eg.iti.mad.akalaty.repo;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import eg.iti.mad.akalaty.model.SingleMealItem;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface IMealsRepo {

    //remote
    public Single<RandomMealResponse> getRandomMeal();

    public Single<CategoriesResponse> getAllCategories();
    public Single<FilteredMealsResponse> getMealsByCategory(String catId);

    public Single<AreasResponse> getAllAreas();
    public Single<FilteredMealsResponse> getMealsByArea(String areaId);

    public Single<IngredientsResponse> getAllIngredients();
    public Single<FilteredMealsResponse> getMealsByIngredient(String ingredientId);
    public Single<SingleMealByIdResponse> getMealById(String mealId);

    //local fav
    public Flowable<List<SingleMealItem>> getAllStoredFavMeals();
    public Completable insertMealToFav(SingleMealItem singleMealItem);
    public Completable deleteMealFromFav(SingleMealItem singleMealItem);

    //planned
    public Flowable<List<PlannedMeal>> getPlannedMealsByDate(Date date);
    public Completable insertMealToPlanned(PlannedMeal plannedMeal);
    public Completable deleteMealFromPlanned(PlannedMeal plannedMeal);

}
