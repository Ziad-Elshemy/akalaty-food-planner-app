package eg.iti.mad.akalaty.ui.favorites.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentMyFavBinding;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.favorites.presenter.FavPresenter;


public class MyFavFragment extends Fragment implements IViewMyFavFragment, OnRemoveClickListener {

    FragmentMyFavBinding viewDataBinding;

    FavPresenter favPresenter;

    MyFavAdapter myFavAdapter;

    private static final String TAG = "MyFavFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_fav,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myFavAdapter = new MyFavAdapter(requireContext(),new ArrayList<>(),this);
        viewDataBinding.myFavRecyclerView.setAdapter(myFavAdapter);
        favPresenter = new FavPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));
        favPresenter.getStoredMeals();

    }

    @Override
    public void showFavMeals(List<SingleMealItem> mealsList) {
        myFavAdapter.changeData(mealsList);
    }

    @Override
    public void onRemoveClicked(SingleMealItem singleMealItem) {
        favPresenter.deleteFromFav(singleMealItem);
    }
}