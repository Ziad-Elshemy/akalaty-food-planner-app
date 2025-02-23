package eg.iti.mad.akalaty.api;

import android.util.Log;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.MealDetailsItem;
import eg.iti.mad.akalaty.model.MealDetailsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.model.SingleMealByIdResponse;
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
    public void getRandomMeal(NetworkCallbackRandom networkCallbackRandom){
        webService.getRandomMealDetails().enqueue(new Callback<RandomMealResponse>() {
            @Override
            public void onResponse(Call<RandomMealResponse> call, Response<RandomMealResponse> response) {
                networkCallbackRandom.onSuccessRandomMealResult(response.body().getRandomMeals().get(0));
            }

            @Override
            public void onFailure(Call<RandomMealResponse> call, Throwable throwable) {
                networkCallbackRandom.onFailureRandomMealResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getAllCategories(NetworkCallbackAllCategories networkCallbackAllCategories) {
        webService.getAllCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                networkCallbackAllCategories.onSuccessAllCategoriesResult(response.body().getCategories());
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable throwable) {
                networkCallbackAllCategories.onFailureAllCategoriesResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getMealsByCategoryId(NetworkCallbackMealsByCategory networkCallbackMealsByCategory, String catId) {
        webService.getMealsByCategory(catId).enqueue(new Callback<FilteredMealsResponse>() {
            @Override
            public void onResponse(Call<FilteredMealsResponse> call, Response<FilteredMealsResponse> response) {
                networkCallbackMealsByCategory.onSuccessMealsByCategoryResult(response.body().getFilteredMeals());
            }

            @Override
            public void onFailure(Call<FilteredMealsResponse> call, Throwable throwable) {
                networkCallbackMealsByCategory.onFailureMealsByCategoryResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getAllAreas(NetworkCallbackAllAreas networkCallbackAllAreas) {
        webService.getAllAreas("list").enqueue(new Callback<AreasResponse>() {
            @Override
            public void onResponse(Call<AreasResponse> call, Response<AreasResponse> response) {
                networkCallbackAllAreas.onSuccessAllAreasResult(response.body().getAreas());
            }

            @Override
            public void onFailure(Call<AreasResponse> call, Throwable throwable) {
                networkCallbackAllAreas.onFailureAllAreasResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getMealsByAreaId(NetworkCallbackMealsByArea networkCallbackMealsByArea, String areaId) {
        webService.getMealsByArea(areaId).enqueue(new Callback<FilteredMealsResponse>() {
            @Override
            public void onResponse(Call<FilteredMealsResponse> call, Response<FilteredMealsResponse> response) {
                networkCallbackMealsByArea.onSuccessMealsByAreaResult(response.body().getFilteredMeals());
            }

            @Override
            public void onFailure(Call<FilteredMealsResponse> call, Throwable throwable) {
                networkCallbackMealsByArea.onFailureMealsByAreaResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getAllIngredients(NetworkCallbackAllIngredients networkCallbackAllIngredients) {
        webService.getAllIngredients("list").enqueue(new Callback<IngredientsResponse>() {
            @Override
            public void onResponse(Call<IngredientsResponse> call, Response<IngredientsResponse> response) {
                networkCallbackAllIngredients.onSuccessAllIngredientsResult(response.body().getIngredients());
            }

            @Override
            public void onFailure(Call<IngredientsResponse> call, Throwable throwable) {
                networkCallbackAllIngredients.onFailureAllIngredientsResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getMealsByIngredientId(NetworkCallbackMealsByIngredient networkCallbackMealsByIngredients, String ingredientId) {
        webService.getMealsByIngredient(ingredientId).enqueue(new Callback<FilteredMealsResponse>() {
            @Override
            public void onResponse(Call<FilteredMealsResponse> call, Response<FilteredMealsResponse> response) {
                networkCallbackMealsByIngredients.onSuccessMealsByIngredientResult(response.body().getFilteredMeals());
            }

            @Override
            public void onFailure(Call<FilteredMealsResponse> call, Throwable throwable) {
                networkCallbackMealsByIngredients.onFailureMealsByIngredientResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void searchMealsByName(NetworkCallbackSearchMealsByName networkCallbackSearchMealsByName, String mealName) {
        webService.searchMealsByName(mealName).enqueue(new Callback<MealDetailsResponse>() {
            @Override
            public void onResponse(Call<MealDetailsResponse> call, Response<MealDetailsResponse> response) {
                networkCallbackSearchMealsByName.onSuccessSearchMealsByNameResult(response.body().getMealDetails());
            }

            @Override
            public void onFailure(Call<MealDetailsResponse> call, Throwable throwable) {
                networkCallbackSearchMealsByName.onFailureSearchMealsByNameResult(throwable.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getMealById(NetworkCallbackMealById networkCallbackMealById, String mealId) {
        webService.getMealDetailsById(mealId).enqueue(new Callback<SingleMealByIdResponse>() {
            @Override
            public void onResponse(Call<SingleMealByIdResponse> call, Response<SingleMealByIdResponse> response) {
                networkCallbackMealById.onSuccessMealByIdResult(response.body().getSingleMeal().get(0));
                Log.i("testid", "onResponse: "+response);
            }

            @Override
            public void onFailure(Call<SingleMealByIdResponse> call, Throwable throwable) {
                networkCallbackMealById.onFailureMealByIdResult(throwable.getLocalizedMessage());
            }
        });
    }


}
