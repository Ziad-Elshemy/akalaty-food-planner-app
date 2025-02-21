package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse{

	@SerializedName("categories")
	private List<CategoriesItem> categories;

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	@Override
 	public String toString(){
		return 
			"CategoriesResponse{" + 
			"categories = '" + categories + '\'' + 
			"}";
		}
}