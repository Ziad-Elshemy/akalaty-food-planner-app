package eg.iti.mad.akalaty.ui.login.view;

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

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.auth.OnLoginResponse;
import eg.iti.mad.akalaty.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment implements OnLoginResponse {


    private static final String TAG = "LoginFragment";
    FragmentLoginBinding viewDataBinding;

    private MyFirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = new MyFirebaseAuth();

//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
//        }

        viewDataBinding.txtToRegister.setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        viewDataBinding.btnLogin.setOnClickListener(view1 -> {
            signInAccount();
        });

    }

    private void signInAccount() {
        if (validate()){
            //create an account to firebase here

            addAccountToFirebase();

            //Snackbar.make(requireContext(),requireView(),"created",Snackbar.LENGTH_SHORT).show();
        }else {
            //Snackbar.make(requireContext(),requireView(),"not created",Snackbar.LENGTH_SHORT).show();
        }
    }
    private void addAccountToFirebase() {

        mAuth.signInToFirebase(viewDataBinding.edtEmail.getEditText().getText().toString(),viewDataBinding.edtPassword.getEditText().getText().toString(),this);

    }

    private boolean validate() {
        boolean isValid = true;


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
    public void setOnLoginResponse(boolean isSuccess, String msg) {
        if (isSuccess){
            Log.i(TAG, "setOnLoginResponse: "+msg);
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
        }else {
            Log.i(TAG, "setOnLoginResponse: "+msg);
        }
    }
}