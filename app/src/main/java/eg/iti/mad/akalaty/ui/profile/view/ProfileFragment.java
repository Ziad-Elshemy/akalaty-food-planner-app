package eg.iti.mad.akalaty.ui.profile.view;

import android.app.Dialog;
import android.content.Intent;
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

import java.util.concurrent.TimeUnit;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.auth.FirebaseDataSource;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentProfileBinding;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.ui.profile.presenter.ProfilePresenter;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import eg.iti.mad.akalaty.utils.SharedPref;
import eg.iti.mad.akalaty.utils.Utils;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        observeInternetStatus();
        profilePresenter = new ProfilePresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireActivity()), new FirebaseDataSource()),requireContext());

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
            viewDataBinding.progressBarUpload.setVisibility(View.VISIBLE);
            viewDataBinding.btnBackup.setVisibility(View.INVISIBLE);
            viewDataBinding.btnDownload.setEnabled(false);
            profilePresenter.uploadDataToFirestore(SharedPref.getInstance(requireActivity()).getUserId());
        });

        viewDataBinding.btnDownload.setOnClickListener(view1 -> {
            viewDataBinding.progressBarDownload.setVisibility(View.VISIBLE);
            viewDataBinding.btnDownload.setVisibility(View.INVISIBLE);
            viewDataBinding.btnBackup.setEnabled(false);
            profilePresenter.downloadDataFromFirestore(SharedPref.getInstance(requireActivity()).getUserId());
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profilePresenter.clearDisposables();
        disposables.clear();
    }

    private void observeInternetStatus() {
        disposables.add(
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .map(tick -> NetworkUtils.isInternetAvailable(requireContext()))
                        .distinctUntilChanged()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateUi)
        );
    }

    private void updateUi(boolean isConnected) {
        if (isConnected) {
            viewDataBinding.noInternetScene.setVisibility(View.GONE);
            viewDataBinding.profileScene.setVisibility(View.VISIBLE);
        } else {
            viewDataBinding.noInternetScene.setVisibility(View.VISIBLE);
            viewDataBinding.profileScene.setVisibility(View.GONE);
        }
    }

    private void showLogoutDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText(R.string.are_you_sure_you_want_to_logout);
        Button logout = dialog.findViewById(R.id.btnAction);
        logout.setText(R.string.log_out);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                viewDataBinding.progressBarLogout.setVisibility(View.VISIBLE);
                viewDataBinding.btnLogout.setVisibility(View.INVISIBLE);

                profilePresenter.logout(requireActivity());

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
//        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
        viewDataBinding.progressBarUpload.setVisibility(View.INVISIBLE);
        viewDataBinding.btnBackup.setVisibility(View.VISIBLE);
        viewDataBinding.btnDownload.setEnabled(true);
        Utils.showCustomSnackbar(requireView(),msg);
    }

    @Override
    public void showOnUploadFailure(String msg) {
//        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
        viewDataBinding.progressBarUpload.setVisibility(View.INVISIBLE);
        viewDataBinding.btnBackup.setVisibility(View.VISIBLE);
        viewDataBinding.btnDownload.setEnabled(true);
        Utils.showCustomSnackbar(requireView(),msg);
    }

    @Override
    public void showOnDownloadSuccess(String msg) {
        viewDataBinding.progressBarDownload.setVisibility(View.INVISIBLE);
        viewDataBinding.btnDownload.setVisibility(View.VISIBLE);
        viewDataBinding.btnBackup.setEnabled(true);
        Utils.showCustomSnackbar(requireView(),msg);
    }

    @Override
    public void showOnDownloadFailure(String msg) {
        viewDataBinding.progressBarDownload.setVisibility(View.INVISIBLE);
        viewDataBinding.btnDownload.setVisibility(View.VISIBLE);
        viewDataBinding.btnBackup.setEnabled(true);
        Utils.showCustomSnackbar(requireView(),msg);
    }

    @Override
    public void showOnLogoutSuccess(String msg) {
        viewDataBinding.progressBarLogout.setVisibility(View.INVISIBLE);
        viewDataBinding.btnLogout.setVisibility(View.VISIBLE);
        clearSharedPrefData();
        Utils.showCustomSnackbar(requireView(),msg);
//        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showOnLogoutFailure(String msg) {
        viewDataBinding.progressBarLogout.setVisibility(View.INVISIBLE);
        viewDataBinding.btnLogout.setVisibility(View.VISIBLE);
        Utils.showCustomSnackbar(requireView(),msg);
    }

    private void clearSharedPrefData() {
        SharedPref.getInstance(requireActivity()).setIsLogged(false);
        SharedPref.getInstance(requireActivity()).setUserId("");
        SharedPref.getInstance(requireActivity()).setUserName("");
        SharedPref.getInstance(requireActivity()).setUserEmail("");
    }
}