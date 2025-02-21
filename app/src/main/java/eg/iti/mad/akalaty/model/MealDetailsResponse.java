package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MealDetailsResponse{

	@SerializedName("meals")
	private List<MealDetailsItem> mealDetails;

	public List<MealDetailsItem> getMealDetails(){
		return mealDetails;
	}

	@Override
 	public String toString(){
		return 
			"MealDetailsResponse{" + 
			"meals = '" + mealDetails + '\'' +
			"}";
		}
}