package eg.iti.mad.akalaty.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "planned_meals_table")
public class PlannedMeal {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo
    private int id;

    @ColumnInfo
    private SingleMealItem meal;

    @ColumnInfo
    private Date date;

    public PlannedMeal(){}
    public PlannedMeal(Date date, SingleMealItem meal) {
        this.date = date;
        this.meal = meal;
        this.id = id;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public SingleMealItem getMeal() {
        return meal;
    }

    public void setMeal(SingleMealItem meal) {
        this.meal = meal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}