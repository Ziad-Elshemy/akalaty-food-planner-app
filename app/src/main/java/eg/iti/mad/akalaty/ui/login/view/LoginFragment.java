package eg.iti.mad.akalaty.ui.login.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.auth.OnLoginResponse;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentLoginBinding;
import eg.iti.mad.akalaty.model.AppUser;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.ui.login.presenter.LoginPresenter;
import eg.iti.mad.akalaty.utils.SharedPref;
import eg.iti.mad.akalaty.utils.Utils;


import androidx.credentials.CredentialManager;


public class LoginFragment extends Fragment implements OnLoginResponse, IViewLoginFragment {


    private static final String TAG = "LoginFragment";
    FragmentLoginBinding viewDataBinding;
    Dialog dialog;
    LoginPresenter loginPresenter;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_credential_manager]
    private CredentialManager credentialManager;
    // [END declare_credential_manager]


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_credential_manager]
        // Initialize Credential Manager
        credentialManager = CredentialManager.create(requireContext());
        // [END initialize_credential_manager]


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isLogged = SharedPref.getInstance(requireActivity()).getIsLogged();

        if (isLogged) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
        }

        loginPresenter = new LoginPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));


        viewDataBinding.txtToRegister.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        viewDataBinding.txtSkip.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        });

        viewDataBinding.btnLogin.setOnClickListener(view1 -> {
            signInAccount();
        });

        viewDataBinding.cardGoogleLogin.setOnClickListener(view1 -> {
//            launchCredentialManager();
            viewDataBinding.btnLogin.setVisibility(View.INVISIBLE);
            viewDataBinding.cardGoogleLogin.setVisibility(View.INVISIBLE);
            viewDataBinding.progressBarLogin.setVisibility(View.VISIBLE);
            loginPresenter.signInWithGoogle(requireContext(), credentialManager,this);
        });

    }


    private void signInAccount() {
        if (validate()) {
            //create an account to firebase here
            addAccountToFirebase();

            viewDataBinding.btnLogin.setVisibility(View.INVISIBLE);
            viewDataBinding.progressBarLogin.setVisibility(View.VISIBLE);
        } else {
        }
    }

    private void addAccountToFirebase() {

        loginPresenter.addAccountToFirebase(viewDataBinding.edtEmail.getEditText().getText().toString(), viewDataBinding.edtPassword.getEditText().getText().toString(), this);

    }

    private boolean validate() {
        boolean isValid = true;


        if (viewDataBinding.edtEmail.getEditText().getText().toString().isEmpty()) {
            //set error in email;
            viewDataBinding.edtEmail.setError("Please Enter Your Email");
            isValid = false;
        } else {
            viewDataBinding.edtEmail.setError(null);
        }

        if (viewDataBinding.edtPassword.getEditText().getText().toString().isEmpty()) {
            //set error in password;
            viewDataBinding.edtPassword.setError("Please Enter Your Password");
            isValid = false;
        } else {
            viewDataBinding.edtPassword.setError(null);
        }

        return isValid;
    }


    private void saveUserToSharedPref(AppUser user) {

        SharedPref.getInstance(requireActivity()).setIsLogged(true);
        SharedPref.getInstance(requireActivity()).setUserId(user.getId());
        SharedPref.getInstance(requireActivity()).setUserName(user.getUsername());
        SharedPref.getInstance(requireActivity()).setUserEmail(user.getEmail());
    }


    @Override
    public void setOnLoginSuccess(String userId) {

        Log.i(TAG, "setOnLoginResponse: " + userId);
//            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
        loginPresenter.checkUserFromFirestore(userId);

    }

    @Override
    public void setOnLoginFailure(String msg) {
        Log.i(TAG, "setOnLoginFailure: " + msg);
        viewDataBinding.btnLogin.setVisibility(View.VISIBLE);
        viewDataBinding.progressBarLogin.setVisibility(View.INVISIBLE);
        showLoginFailedPopup(msg);
    }

    private void showLoginFailedPopup(String msg) {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText(msg);
        Button createAccount = dialog.findViewById(R.id.btnAction);
        createAccount.setText(R.string.create_account);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewDataBinding.btnLogin.setVisibility(View.VISIBLE);
                viewDataBinding.cardGoogleLogin.setVisibility(View.VISIBLE);
                viewDataBinding.progressBarLogin.setVisibility(View.INVISIBLE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewDataBinding.btnLogin.setVisibility(View.VISIBLE);
                viewDataBinding.cardGoogleLogin.setVisibility(View.VISIBLE);
                viewDataBinding.progressBarLogin.setVisibility(View.INVISIBLE);
            }
        });

        dialog.show();
    }

    @Override
    public void showOnDataFetchedFromFirestore() {
//        Toast.makeText(requireContext(), "Data Fetched From Firestore", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOnUserLoginSuccess(AppUser appUser) {
        // now you can save your user and auto login
        saveUserToSharedPref(appUser);
        viewDataBinding.progressBarLogin.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showOnUserLoginFailure(String errMsg) {
        Utils.showCustomSnackbar(requireView(), errMsg);
        showLoginFailedPopup(errMsg);
    }


    @Override
    public void showOnUserLoginFailureWithGoogle(String errMsg) {
        Log.i(TAG, "showOnUserLoginFailureWithGoogle: " + errMsg);
        new Handler(Looper.getMainLooper()).post(() -> {
            showLoginFailedPopup(errMsg);
            viewDataBinding.btnLogin.setVisibility(View.VISIBLE);
            viewDataBinding.cardGoogleLogin.setVisibility(View.VISIBLE);
            viewDataBinding.progressBarLogin.setVisibility(View.INVISIBLE);
        });
    }
}