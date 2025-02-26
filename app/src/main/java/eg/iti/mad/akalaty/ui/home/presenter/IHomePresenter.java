package eg.iti.mad.akalaty.ui.home.presenter;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IHomePresenter {
    void getRandomMeal();
    void getAllCategories();
    void getMealsByCategory(String catId);
    void getAllAreas();
    void getMealsByArea(String areaId);
    void getAllIngredient();
    void getMealsByIngredient(String ingredientId);

    void getMealById(String mealId);

}
