package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SingleMealByIdResponse{

	@SerializedName("meals")
	private List<SingleMealItem> singleMeal;

	public List<SingleMealItem> getSingleMeal(){
		return singleMeal;
	}

	@Override
 	public String toString(){
		return 
			"SingleMealByIdResponse{" + 
			"singleMeal = '" + singleMeal + '\'' + 
			"}";
		}
}