package eg.iti.mad.akalaty.ui.calender.view;

import java.util.List;

import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;

public interface IViewCalendarFragment {
    public void showPlannedMeals(List<PlannedMeal> mealsList);
}
