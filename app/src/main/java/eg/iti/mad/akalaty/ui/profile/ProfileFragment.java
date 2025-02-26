package eg.iti.mad.akalaty.ui.profile;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.databinding.FragmentProfileBinding;
import eg.iti.mad.akalaty.utils.SharedPref;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding viewDataBinding;
    Dialog dialog;

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

        });

        viewDataBinding.btnBackup.setOnClickListener(view1 -> {

        });

    }

    private void showLogoutDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete_layout);
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
                SharedPref.getInstance(requireActivity()).setIsLogged(false);
                SharedPref.getInstance(requireActivity()).setUserId("");
                SharedPref.getInstance(requireActivity()).setUserName("");
                SharedPref.getInstance(requireActivity()).setUserEmail("");

                Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_loginFragment);
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

}