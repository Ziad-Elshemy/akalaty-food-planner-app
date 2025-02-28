package eg.iti.mad.akalaty.ui.register.view;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.api.RemoteDataSource;
import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.database.MealsLocalDataSource;
import eg.iti.mad.akalaty.databinding.FragmentRegisterBinding;
import eg.iti.mad.akalaty.auth.OnRegisterResponse;
import eg.iti.mad.akalaty.model.AppUser;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.ui.register.presenter.RegisterPresenter;
import eg.iti.mad.akalaty.utils.SharedPref;


public class RegisterFragment extends Fragment implements IViewRegisterFragment, OnRegisterResponse {

    private static final String TAG = "Firebase";
    FragmentRegisterBinding viewDataBinding;
    RegisterPresenter registerPresenter;

    Dialog dialog;
    MyFirebaseAuth myFirebaseAuth;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_credential_manager]
    private CredentialManager credentialManager;
    // [END declare_credential_manager]


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFirebaseAuth = new MyFirebaseAuth();

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
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerPresenter = new RegisterPresenter(this, MealsRepo.getInstance(RemoteDataSource.getInstance(), MealsLocalDataSource.getInstance(requireContext())));

        viewDataBinding.txtToLogin.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
        });

        viewDataBinding.txtSkip.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_homeFragment);
        });


        viewDataBinding.btnSignUp.setOnClickListener(view1 -> {
            createAccount();
            Log.i(TAG, "onViewCreated: ");
        });

        viewDataBinding.cardGoogle.setOnClickListener(view1 -> {
            viewDataBinding.btnSignUp.setVisibility(View.INVISIBLE);
            viewDataBinding.cardGoogle.setVisibility(View.INVISIBLE);
            viewDataBinding.progressBarSignup.setVisibility(View.VISIBLE);
            registerPresenter.signUpWithGoogle(requireContext(), credentialManager);
        });

    }


    void createAccount(){
        if (validate()){
            //create an account to firebase here
            viewDataBinding.btnSignUp.setVisibility(View.INVISIBLE);
            viewDataBinding.progressBarSignup.setVisibility(View.VISIBLE);
            addAccountToFirebase();
            
            //Snackbar.make(requireContext(),requireView(),"created",Snackbar.LENGTH_SHORT).show();
        }else {
            //Snackbar.make(requireContext(),requireView(),"not created",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void addAccountToFirebase() {

        myFirebaseAuth.registerToFirebase(viewDataBinding.edtEmail.getEditText().getText().toString(), viewDataBinding.edtUsername.getEditText().getText().toString(),viewDataBinding.edtPassword.getEditText().getText().toString(),this);
    }

    private boolean validate() {
        boolean isValid = true;

        if (viewDataBinding.edtUsername.getEditText().getText().toString().isEmpty()){
            //set error in username;
            viewDataBinding.edtUsername.setError("Please Enter Your Username");
            isValid = false;
        }else {
            viewDataBinding.edtUsername.setError(null);
        }

        if (viewDataBinding.edtEmail.getEditText().getText().toString().isEmpty()){
            //set error in email;
            viewDataBinding.edtEmail.setError("Please Enter Your Email");
            isValid = false;
        }else {
            viewDataBinding.edtEmail.setError(null);
        }

        if (viewDataBinding.edtPassword.getEditText().getText().toString().isEmpty()){
            //set error in password;
            viewDataBinding.edtPassword.setError("Please Enter Your Password");
            isValid = false;
        }else {
            viewDataBinding.edtPassword.setError(null);
        }

        return isValid;
    }


    @Override
    public void setOnRegisterResponse(boolean isSuccess, String msg) {
        if (isSuccess){
            Log.i(TAG, "setOnRegisterResponse: "+msg);
            viewDataBinding.btnSignUp.setVisibility(View.VISIBLE);
            viewDataBinding.progressBarSignup.setVisibility(View.INVISIBLE);
            showRegisterSuccessPopup("Registered Successfully!");
//            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
        }else {
            Log.i(TAG, "setOnRegisterResponse: "+msg);

            viewDataBinding.progressBarSignup.setVisibility(View.INVISIBLE);
            showRegisterFailedDialog(msg);
        }
    }

    private void showRegisterSuccessPopup(String msg) {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_good_info_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.txtGoodInfo);
        txt.setText(msg);
        Button ok = dialog.findViewById(R.id.btnGoodInfoOk);
        ok.setText("Ok");
        ImageButton close = dialog.findViewById(R.id.btnGoodInfoClose);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        dialog.show();
    }

    private void showRegisterFailedDialog(String msg) {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_bad_info_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.txtBadInfo);
        txt.setText(msg);
        Button tryAgain = dialog.findViewById(R.id.btnBadInfoOk);
        tryAgain.setText("Try Again");
        ImageButton close = dialog.findViewById(R.id.btnBadInfoClose);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                viewDataBinding.btnSignUp.setVisibility(View.VISIBLE);
                viewDataBinding.progressBarSignup.setVisibility(View.INVISIBLE);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        dialog.show();
    }

    private void saveUserToSharedPref(AppUser user) {

        SharedPref.getInstance(requireActivity()).setIsLogged(true);
        SharedPref.getInstance(requireActivity()).setUserId(user.getId());
        SharedPref.getInstance(requireActivity()).setUserName(user.getUsername());
        SharedPref.getInstance(requireActivity()).setUserEmail(user.getEmail());
    }

    @Override
    public void showOnUserRegisterSuccessWithGoogle(AppUser appUser) {
        Log.i(TAG, "showOnUserLoginSuccessWithGoogle: " + appUser.getEmail());
        // now you can save your user and auto login
        saveUserToSharedPref(appUser);
        viewDataBinding.progressBarSignup.setVisibility(View.GONE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showOnUserRegisterFailureWithGoogle(String errMsg) {
        Log.i(TAG, "showOnUserLoginFailureWithGoogle: " + errMsg);
        new Handler(Looper.getMainLooper()).post(() -> {
            viewDataBinding.btnSignUp.setVisibility(View.VISIBLE);
            viewDataBinding.cardGoogle.setVisibility(View.VISIBLE);
            viewDataBinding.progressBarSignup.setVisibility(View.INVISIBLE);
            showRegisterFailedDialog(errMsg);
        });
    }
}