package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class IngredientsResponse{

	@SerializedName("meals")
	private List<IngredientsItem> ingredients;

	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	@Override
 	public String toString(){
		return 
			"IngredientsResponse{" + 
			"ingredients = '" + ingredients + '\'' + 
			"}";
		}
}