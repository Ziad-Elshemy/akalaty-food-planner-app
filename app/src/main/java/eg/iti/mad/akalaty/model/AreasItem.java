package eg.iti.mad.akalaty.model;

import com.google.gson.annotations.SerializedName;

public class AreasItem{

	@SerializedName("strArea")
	private String strArea;

	public String getStrArea(){
		return strArea;
	}

	@Override
 	public String toString(){
		return 
			"AreasItem{" + 
			"strArea = '" + strArea + '\'' + 
			"}";
		}
}