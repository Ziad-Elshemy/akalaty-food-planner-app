package eg.iti.mad.akalaty.ui.favorites.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentMyFavBinding;
import eg.iti.mad.akalaty.model.SingleMealItem;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.favorites.presenter.FavPresenter;
import eg.iti.mad.akalaty.utils.Utils;


public class MyFavFragment extends Fragment implements IViewMyFavFragment, OnRemoveClickListener {

    FragmentMyFavBinding viewDataBinding;

    FavPresenter favPresenter;

    MyFavAdapter myFavAdapter;

    Dialog dialog;

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
    public void showOnDeleteSuccess(String successMsg) {
        Utils.showCustomSnackbar(requireView(),successMsg);
    }

    @Override
    public void showOnDeleteFailure(String failureMsg) {
        Utils.showCustomSnackbar(requireView(),failureMsg);
    }

    @Override
    public void onRemoveClicked(SingleMealItem singleMealItem) {
        showDeletePopup(singleMealItem);
    }


    private void showDeletePopup(SingleMealItem singleMealItem) {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText("Delete this item?");
        Button delete = dialog.findViewById(R.id.btnAction);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                favPresenter.deleteFromFav(singleMealItem);
                Utils.showCustomSnackbar(requireView(),"Deleted Successfully");
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Utils.showCustomSnackbar(requireView(),"Canceled");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Utils.showCustomSnackbar(requireView(),"Canceled");
            }
        });

        dialog.show();
    }


}