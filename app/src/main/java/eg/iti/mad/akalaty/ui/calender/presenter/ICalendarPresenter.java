package eg.iti.mad.akalaty.ui.calender.presenter;

import java.util.Date;

import eg.iti.mad.akalaty.model.PlannedMeal;

public interface ICalendarPresenter {

    public void getPlannedMealsByDate(Date date);
    public void deleteFromCal(PlannedMeal plannedMeal);

}
