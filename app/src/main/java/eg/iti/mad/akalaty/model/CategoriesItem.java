package eg.iti.mad.akalaty.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoriesItem {

	@SerializedName("strCategory")
	private String strCategory;

	@SerializedName("strCategoryDescription")
	private String strCategoryDescription;

	@SerializedName("idCategory")
	private String idCategory;

	@SerializedName("strCategoryThumb")
	private String strCategoryThumb;

	public String getStrCategory(){
		return strCategory;
	}

	public String getStrCategoryDescription(){
		return strCategoryDescription;
	}

	public String getIdCategory(){
		return idCategory;
	}

	public String getStrCategoryThumb(){
		return strCategoryThumb;
	}

	@Override
 	public String toString(){
		return 
			"CategoriesItem{" + 
			"strCategory = '" + strCategory + '\'' + 
			",strCategoryDescription = '" + strCategoryDescription + '\'' + 
			",idCategory = '" + idCategory + '\'' + 
			",strCategoryThumb = '" + strCategoryThumb + '\'' + 
			"}";
		}
}