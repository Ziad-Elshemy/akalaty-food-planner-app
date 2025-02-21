package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.CategoriesItem;


public interface NetworkCallbackAllCategories {
    public void onSuccessAllCategoriesResult(List<CategoriesItem> categoriesItem);
    public void onFailureAllCategoriesResult(String errorMsg);
}
