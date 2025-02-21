package eg.iti.mad.akalaty.api;

import java.util.List;

import eg.iti.mad.akalaty.model.AreasItem;
import eg.iti.mad.akalaty.model.CategoriesItem;


public interface NetworkCallbackAllAreas {
    public void onSuccessAllAreasResult(List<AreasItem> areasItems);
    public void onFailureAllAreasResult(String errorMsg);
}
