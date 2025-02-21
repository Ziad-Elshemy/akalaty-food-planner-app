package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FilteredMealsResponse{

	@SerializedName("meals")
	private List<FilteredMealsItem> filteredMeals;

	public List<FilteredMealsItem> getFilteredMeals(){
		return filteredMeals;
	}

	@Override
 	public String toString(){
		return 
			"FilteredMealsResponse{" + 
			"filteredMeals = '" + filteredMeals + '\'' + 
			"}";
		}
}