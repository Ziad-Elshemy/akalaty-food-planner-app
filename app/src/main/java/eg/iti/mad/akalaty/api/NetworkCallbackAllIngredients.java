package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.IngredientsItem;


public interface NetworkCallbackAllIngredients {
    public void onSuccessAllIngredientsResult(List<IngredientsItem> ingredientsItems);
    public void onFailureAllIngredientsResult(String errorMsg);
}
