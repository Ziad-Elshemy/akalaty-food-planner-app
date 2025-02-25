package eg.iti.mad.akalaty.ui.search.presenter;

public interface ISearchPresenter {
    void getAllCategories();
    void getMealsByCategory(String catId);
    void getAllAreas();

    void getMealsByArea(String areaId);
    void getAllIngredient();
    void getMealsByIngredient(String ingredientId);



}
