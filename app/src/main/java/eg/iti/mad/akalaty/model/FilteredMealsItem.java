package eg.iti.mad.akalaty.model;

import com.google.gson.annotations.SerializedName;

public class FilteredMealsItem{

	@SerializedName("strMealThumb")
	private String strMealThumb;

	@SerializedName("idMeal")
	private String idMeal;

	@SerializedName("strMeal")
	private String strMeal;

	public String getStrMealThumb(){
		return strMealThumb;
	}

	public String getIdMeal(){
		return idMeal;
	}

	public String getStrMeal(){
		return strMeal;
	}

	public void setStrMealThumb(String strMealThumb) {
		this.strMealThumb = strMealThumb;
	}

	public void setIdMeal(String idMeal) {
		this.idMeal = idMeal;
	}

	public void setStrMeal(String strMeal) {
		this.strMeal = strMeal;
	}

	@Override
 	public String toString(){
		return 
			"FilteredMealsItem{" + 
			"strMealThumb = '" + strMealThumb + '\'' + 
			",idMeal = '" + idMeal + '\'' + 
			",strMeal = '" + strMeal + '\'' + 
			"}";
		}
}