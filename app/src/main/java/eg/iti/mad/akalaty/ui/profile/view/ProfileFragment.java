package eg.iti.mad.akalaty.ui.profile.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentProfileBinding;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.profile.presenter.ProfilePresenter;
import eg.iti.mad.akalaty.utils.SharedPref;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ProfileFragment extends Fragment  implements IViewProfileFragment{

    FragmentProfileBinding viewDataBinding;
    Dialog dialog;
    private final CompositeDisposable disposables = new CompositeDisposable();

    ProfilePresenter profilePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePresenter = new ProfilePresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireActivity())),requireContext());

        viewDataBinding.txtUsername.setText(SharedPref.getInstance(requireActivity()).getUserName());
        viewDataBinding.txtEmail.setText(SharedPref.getInstance(requireActivity()).getUserEmail());

        viewDataBinding.btnYourFav.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.popBackStack();
            navController.navigate(R.id.myFavFragment);

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.myFavFragment);
        });

        viewDataBinding.btnYourPlans.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.popBackStack();
            navController.navigate(R.id.calenderFragment);

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.calenderFragment);
        });

        viewDataBinding.btnLogout.setOnClickListener(view1 -> {
            showLogoutDialog();
        });

        viewDataBinding.btnBackup.setOnClickListener(view1 -> {

            profilePresenter.uploadDataToFirestore(SharedPref.getInstance(requireActivity()).getUserId());
        });

        viewDataBinding.btnDownload.setOnClickListener(view1 -> {
            profilePresenter.downloadDataFromFirestore(SharedPref.getInstance(requireActivity()).getUserId());
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profilePresenter.clearDisposables();
    }

    private void showLogoutDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText("Are you sure you \nwant to logout?");
        Button logout = dialog.findViewById(R.id.btnAction);
        logout.setText("Logout");
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                profilePresenter.logout();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void showOnUploadSuccess(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnUploadFailure(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnDownloadSuccess(String msg) {

    }

    @Override
    public void showOnDownloadFailure(String msg) {

    }

    @Override
    public void showOnLogoutSuccess(String msg) {
        SharedPref.getInstance(requireActivity()).setIsLogged(false);
        SharedPref.getInstance(requireActivity()).setUserId("");
        SharedPref.getInstance(requireActivity()).setUserName("");
        SharedPref.getInstance(requireActivity()).setUserEmail("");
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment);
    }

    @Override
    public void showOnLogoutFailure(String msg) {

    }
}