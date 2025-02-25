package eg.iti.mad.akalaty.ui.home.presenter;

import eg.iti.mad.akalaty.model.AreasResponse;
import eg.iti.mad.akalaty.model.CategoriesResponse;
import eg.iti.mad.akalaty.model.FilteredMealsResponse;
import eg.iti.mad.akalaty.model.IngredientsResponse;
import eg.iti.mad.akalaty.model.RandomMealResponse;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.home.view.IViewHomeFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements IHomePresenter{

    IViewHomeFragment _view;
    MealsRepo _repo;

    public HomePresenter(IViewHomeFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
    }


    @Override
    public void getRandomMeal() {
        Single<RandomMealResponse> call = _repo.getRandomMeal();
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getRandomMeals().get(0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            _view.showRandomMeal(item);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getAllCategories() {
        Single<CategoriesResponse> call = _repo.getAllCategories();
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getCategories())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showAllCategories(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getMealsByCategory(String catId) {
        Single<FilteredMealsResponse> call = _repo.getMealsByCategory(catId);
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getFilteredMeals())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showMealsByCategory(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getAllAreas() {
        Single<AreasResponse> call = _repo.getAllAreas();
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getAreas())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showAllAreas(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getMealsByArea(String areaId) {
        Single<FilteredMealsResponse> call = _repo.getMealsByArea(areaId);
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getFilteredMeals())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showMealsByArea(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getAllIngredient() {
        Single<IngredientsResponse> call = _repo.getAllIngredients();
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getIngredients())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showAllIngredients(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

    @Override
    public void getMealsByIngredient(String ingredientId) {
        Single<FilteredMealsResponse> call = _repo.getMealsByIngredient(ingredientId);
        call.subscribeOn(Schedulers.io())
                .map(item -> item.getFilteredMeals())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            _view.showMealsByIngredient(list);
                        },
                        error -> {
                            _view.showErrorMsg(error.getLocalizedMessage());
                        }
                );
    }

}
