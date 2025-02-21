package eg.iti.mad.akalaty.ui.register.controler;

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
import eg.iti.mad.akalaty.databinding.FragmentRegisterBinding;
import eg.iti.mad.akalaty.ui.register.view.OnRegisterResponse;


public class RegisterFragment extends Fragment implements OnRegisterResponse {

    private static final String TAG = "Firebase";
    FragmentRegisterBinding viewDataBinding;

    MyFirebaseAuth myFirebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFirebaseAuth = new MyFirebaseAuth();

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


        viewDataBinding.btnSignUp.setOnClickListener(view1 -> {
            createAccount();
        });

    }

    void createAccount(){
        if (validate()){
            //create an account to firebase here
            
            addAccountToFirebase();
            
            //Snackbar.make(requireContext(),requireView(),"created",Snackbar.LENGTH_SHORT).show();
        }else {
            //Snackbar.make(requireContext(),requireView(),"not created",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void addAccountToFirebase() {

        myFirebaseAuth.registerToFirebase(viewDataBinding.edtEmail.getEditText().getText().toString(),viewDataBinding.edtPassword.getEditText().getText().toString(),this);

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
            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
        }else {
            Log.i(TAG, "setOnRegisterResponse: "+msg);
        }
    }
}