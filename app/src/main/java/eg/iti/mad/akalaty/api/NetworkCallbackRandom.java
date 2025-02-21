package eg.iti.mad.akalaty.api;

import eg.iti.mad.akalaty.model.RandomMealsItem;


public interface NetworkCallbackRandom {
    public void onSuccessRandomMealResult(RandomMealsItem randomMeal);
    public void onFailureRandomMealResult(String errorMsg);
}
