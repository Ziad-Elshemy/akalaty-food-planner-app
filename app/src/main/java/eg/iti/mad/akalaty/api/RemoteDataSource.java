package eg.iti.mad.akalaty.api;

import android.util.Log;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource implements IRemoteDataSource{
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String TAG = "ProductsRemoteDataSource";
    private WebService webService;
    private static RemoteDataSource remoteDataSource = null;


    private RemoteDataSource(){
        // Create Retrofit
        Retrofit retrofit;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e("api", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        webService = retrofit.create(WebService.class);
    }

    public static RemoteDataSource getInstance() {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSource();
        }
        return remoteDataSource;
    }

    @Override
    public Single<RandomMealResponse> getRandomMeal(){
        return webService.getRandomMealDetails();
    }

    @Override
    public Single<CategoriesResponse> getAllCategories() {
        return webService.getAllCategories();
    }

    @Override
    public Single<FilteredMealsResponse> getMealsByCategoryId( String catId) {
        return webService.getMealsByCategory(catId);
    }

    @Override
    public Single<AreasResponse> getAllAreas() {

        return webService.getAllAreas("list");
    }

    @Override
    public Single<FilteredMealsResponse> getMealsByAreaId( String areaId) {
        return webService.getMealsByArea(areaId);
    }

    @Override
    public Single<IngredientsResponse> getAllIngredients() {
        return webService.getAllIngredients("list");
    }

    @Override
    public Single<FilteredMealsResponse> getMealsByIngredientId( String ingredientId) {
        return webService.getMealsByIngredient(ingredientId);
    }

    @Override
    public Single<FilteredMealsResponse> searchMealsByName(String mealName) {
        return webService.searchMealsByName(mealName);
    }

    @Override
    public Single<SingleMealByIdResponse> getMealById( String mealId) {
        return webService.getMealDetailsById(mealId);
    }


}
