package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RandomMealResponse{

	@SerializedName("meals")
	private List<RandomMealsItem> randomMeals;

	public List<RandomMealsItem> getRandomMeals(){
		return randomMeals;
	}

	@Override
 	public String toString(){
		return 
			"RandomMealResponse{" + 
			"randomMeals = '" + randomMeals + '\'' + 
			"}";
		}
}