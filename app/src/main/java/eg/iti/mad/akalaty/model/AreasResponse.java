package eg.iti.mad.akalaty.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AreasResponse{

	@SerializedName("meals")
	private List<AreasItem> areas;

	public List<AreasItem> getAreas(){
		return areas;
	}

	@Override
 	public String toString(){
		return 
			"AreasResponse{" + 
			"areas = '" + areas + '\'' + 
			"}";
		}
}