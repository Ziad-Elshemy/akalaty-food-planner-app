package eg.iti.mad.akalaty.repo;

import android.util.Log;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
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



    //remote
    @Override
    public Single<RandomMealResponse> getRandomMeal() {
        return remoteDataSource.getRandomMeal();
    }

    @Override
    public Single<CategoriesResponse> getAllCategories() {
        return remoteDataSource.getAllCategories();

    }

    @Override
    public Single<FilteredMealsResponse> getMealsByCategory(String catId) {
        return  remoteDataSource.getMealsByCategoryId(catId);

    }

    @Override
    public Single<AreasResponse> getAllAreas() {
        return remoteDataSource.getAllAreas();
    }

    @Override
    public Single<FilteredMealsResponse> getMealsByArea(String areaId) {
        return remoteDataSource.getMealsByAreaId(areaId);
    }

    @Override
    public Single<IngredientsResponse> getAllIngredients() {
        return  remoteDataSource.getAllIngredients();
    }

    @Override
    public Single<FilteredMealsResponse> getMealsByIngredient(String ingredientId) {
        return remoteDataSource.getMealsByIngredientId(ingredientId);
    }

    @Override
    public Single<SingleMealByIdResponse> getMealById(String mealId) {
        Log.i("testid", "getMealById: "+mealId);
        return remoteDataSource.getMealById(mealId);
    }

    @Override
    public Single<FilteredMealsResponse> getMealsBySearch(String mealName) {
        return remoteDataSource.searchMealsByName(mealName);
    }


    // local
    @Override
    public Flowable<List<SingleMealItem>> getAllStoredFavMeals() {
        return mealsLocalDataSource.getAllMeals();
    }

    @Override
    public Completable insertMealToFav(SingleMealItem singleMealItem) {
        return mealsLocalDataSource.insertMeal(singleMealItem);
    }

    @Override
    public Completable deleteMealFromFav(SingleMealItem singleMealItem) {
        return mealsLocalDataSource.deleteMeal(singleMealItem);
    }

    @Override
    public Completable insertAllFav(List<SingleMealItem> singleMealItemList) {
        return mealsLocalDataSource.insertAll(singleMealItemList);
    }

    @Override
    public Completable deleteAllFav() {
        return mealsLocalDataSource.deleteAllFav();
    }

    @Override
    public Single<SingleMealItem> getStoredMealsById(String mealId) {
        return mealsLocalDataSource.getMealById(mealId);
    }

    @Override
    public Flowable<List<PlannedMeal>> getPlannedMealsByDate(Date date) {
        return mealsLocalDataSource.getMealByDate(date);
    }

    @Override
    public Completable insertMealToPlanned(PlannedMeal plannedMeal) {
        return mealsLocalDataSource.insertPlannedMeal(plannedMeal);
    }

    @Override
    public Completable deleteMealFromPlanned(PlannedMeal plannedMeal) {
        return mealsLocalDataSource.deletePlannedMeal(plannedMeal);
    }

    @Override
    public Single<PlannedMeal> getPlannedMealsById(String mealId) {
        return mealsLocalDataSource.getPlanedMealById(mealId);
    }

    @Override
    public Flowable<List<PlannedMeal>> getAllStoredPlannedMeals() {
        return mealsLocalDataSource.getAllPlannedMeals();
    }

    @Override
    public Completable insertAllPlanned(List<PlannedMeal> plannedMealList) {
        return mealsLocalDataSource.insertAllPlanned(plannedMealList);
    }

    @Override
    public Completable deleteAllPlanned() {
        return mealsLocalDataSource.deleteAllPlanned();
    }
}
