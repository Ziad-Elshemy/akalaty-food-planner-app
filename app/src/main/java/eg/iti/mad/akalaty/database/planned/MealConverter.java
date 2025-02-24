package eg.iti.mad.akalaty.database.planned;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.Date;

import eg.iti.mad.akalaty.model.SingleMealItem;

public class MealConverter {
    @TypeConverter
    public String fromMeal(SingleMealItem meal){
        return new Gson().toJson(meal);
    }

    @TypeConverter
    public SingleMealItem toMeal(String json){
        return new  Gson().fromJson(json,SingleMealItem.class);
    }
}
