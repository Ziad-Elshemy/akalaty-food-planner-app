package eg.iti.mad.akalaty.repo;

import android.util.Log;

import java.util.Date;
import java.util.List;

import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.auth.FirebaseDataSource;
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
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.util.Pair;

public class MealsRepo implements IMealsRepo{
    RemoteDataSource remoteDataSource;
    MealsLocalDataSource mealsLocalDataSource;

    FirebaseDataSource firebaseDataSource;

    private static MealsRepo repo = null;

    private MealsRepo(RemoteDataSource remoteDataSource, MealsLocalDataSource mealsLocalDataSource, FirebaseDataSource firebaseDataSource){
        this.remoteDataSource = remoteDataSource;
        this.mealsLocalDataSource = mealsLocalDataSource;
        this.firebaseDataSource = firebaseDataSource;
    }

    public static MealsRepo getInstance(RemoteDataSource remoteDataSource, MealsLocalDataSource mealsLocalDataSource, FirebaseDataSource firebaseDataSource){
        if (repo==null){
            repo = new MealsRepo(remoteDataSource,mealsLocalDataSource,firebaseDataSource);
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


    public Completable uploadDataToFirestore(String userId) {
        return Single.zip(
                mealsLocalDataSource.getAllMeals().firstOrError().subscribeOn(Schedulers.io()),
                mealsLocalDataSource.getAllPlannedMeals().firstOrError().subscribeOn(Schedulers.io()),
                (favMeals, plannedMeals) -> new Pair<>(favMeals, plannedMeals)
        ).flatMapCompletable(data -> firebaseDataSource.uploadData(userId, data.first, data.second));
    }

    public Completable downloadDataFromFirestore(String userId) {
        return firebaseDataSource.downloadUserData(userId)
                .flatMapCompletable(data -> Completable.concatArray(
                        mealsLocalDataSource.deleteAllFav(),
                        mealsLocalDataSource.deleteAllPlanned(),
                        mealsLocalDataSource.insertAll(data.first),
                        mealsLocalDataSource.insertAllPlanned(data.second)
                ));
    }


}
