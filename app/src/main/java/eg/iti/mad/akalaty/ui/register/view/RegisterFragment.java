package eg.iti.mad.akalaty.ui.register.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.databinding.FragmentRegisterBinding;
import eg.iti.mad.akalaty.auth.OnRegisterResponse;


public class RegisterFragment extends Fragment implements OnRegisterResponse {

    private static final String TAG = "Firebase";
    FragmentRegisterBinding viewDataBinding;

    Dialog dialog;
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
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setMessage(msg).setTitle("ERROR");
//            AlertDialog dialog = builder.create();
//            dialog.show();
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

}