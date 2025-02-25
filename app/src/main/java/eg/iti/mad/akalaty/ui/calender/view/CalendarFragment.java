package eg.iti.mad.akalaty.ui.calender.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentCalendarBinding;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.calender.presenter.CalendarPresenter;


public class CalendarFragment extends Fragment implements IViewCalendarFragment, OnRemovePlannedClickListener {

    FragmentCalendarBinding viewDataBinding;
    CalendarPresenter calendarPresenter;
    CalendarAdapter calendarAdapter;
    Calendar calendar;
    private static final String TAG = "CalendarFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_calendar,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendar = Calendar.getInstance();
        clearCalenderTime();
        calendarAdapter = new CalendarAdapter(requireContext(),new ArrayList<>(),this);
        viewDataBinding.myCalRecyclerView.setAdapter(calendarAdapter);
        calendarPresenter = new CalendarPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));
        calendarPresenter.getPlannedMealsByDate(calendar.getTime());
        Log.i(TAG, "calender1: "+ calendar.getTime().getTime());
        viewDataBinding.calendarView.setOnDateChangeListener((calendarView, year, month, day) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            Log.i(TAG, "calender2: "+ calendar.getTime().getTime());
            calendarPresenter.getPlannedMealsByDate(calendar.getTime());
        });
    }

    private void clearCalenderTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void showPlannedMeals(List<PlannedMeal> mealsList) {
        calendarAdapter.changeData(mealsList);
    }

    @Override
    public void onRemovePlannedClicked(PlannedMeal plannedMeal) {
        calendarPresenter.deleteFromCal(plannedMeal);
    }
}