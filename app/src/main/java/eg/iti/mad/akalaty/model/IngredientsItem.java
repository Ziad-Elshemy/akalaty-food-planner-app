package eg.iti.mad.akalaty.model;

import com.google.gson.annotations.SerializedName;

public class IngredientsItem{

	@SerializedName("strDescription")
	private String strDescription;

	@SerializedName("strIngredient")
	private String strIngredient;

	@SerializedName("strType")
	private Object strType;

	@SerializedName("idIngredient")
	private String idIngredient;

	public String getStrDescription(){
		return strDescription;
	}

	public String getStrIngredient(){
		return strIngredient;
	}

	public Object getStrType(){
		return strType;
	}

	public String getIdIngredient(){
		return idIngredient;
	}

	@Override
 	public String toString(){
		return 
			"IngredientsItem{" + 
			"strDescription = '" + strDescription + '\'' + 
			",strIngredient = '" + strIngredient + '\'' + 
			",strType = '" + strType + '\'' + 
			",idIngredient = '" + idIngredient + '\'' + 
			"}";
		}
}