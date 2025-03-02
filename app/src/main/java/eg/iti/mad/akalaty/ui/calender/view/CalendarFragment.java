package eg.iti.mad.akalaty.ui.calender.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentCalendarBinding;
import eg.iti.mad.akalaty.model.PlannedMeal;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.calender.presenter.CalendarPresenter;
import eg.iti.mad.akalaty.ui.favorites.view.MyFavFragmentDirections;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import eg.iti.mad.akalaty.utils.Utils;


public class CalendarFragment extends Fragment implements IViewCalendarFragment, OnRemovePlannedClickListener {

    FragmentCalendarBinding viewDataBinding;
    CalendarPresenter calendarPresenter;
    CalendarAdapter calendarAdapter;
    Calendar calendar;
    Dialog dialog;
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
    public void showOnDeleteSuccess(String msg) {
        Utils.showCustomSnackbar(requireView(),getString(R.string.deleted_successfully));
    }

    @Override
    public void showOnDeleteFailure(String errMsg) {
        Utils.showCustomSnackbar(requireView(),errMsg);
    }

    @Override
    public void onRemovePlannedClicked(PlannedMeal plannedMeal) {
        showDeletePopup(plannedMeal);
    }

    @Override
    public void onItemClicked(PlannedMeal plannedMeal) {
        CalendarFragmentDirections.ActionCalenderFragmentToMealDetailsFragment action;
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            action= CalendarFragmentDirections.actionCalenderFragmentToMealDetailsFragment(plannedMeal.getMeal().getIdMeal());
        }else {
            action = CalendarFragmentDirections.actionCalenderFragmentToMealDetailsFragment(plannedMeal.getId()+"");
        }

        Navigation.findNavController(viewDataBinding.getRoot()).navigate(action);
    }


    private void showDeletePopup(PlannedMeal plannedMeal) {
    dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
    TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText(R.string.delete_this_item_);
    Button delete = dialog.findViewById(R.id.btnAction);
    Button cancel = dialog.findViewById(R.id.btnCancel);
    ImageButton close = dialog.findViewById(R.id.btnClose);

        delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            calendarPresenter.deleteFromCal(plannedMeal);
        }
    });
        close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            Utils.showCustomSnackbar(requireView(),getString(R.string.canceled));
        }
    });
        cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            Utils.showCustomSnackbar(requireView(),getString(R.string.canceled));
        }
    });

        dialog.show();
}

}