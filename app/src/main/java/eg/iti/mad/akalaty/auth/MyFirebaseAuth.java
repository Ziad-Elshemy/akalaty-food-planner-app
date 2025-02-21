package eg.iti.mad.akalaty.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import eg.iti.mad.akalaty.ui.login.view.OnLoginResponse;
import eg.iti.mad.akalaty.ui.register.view.OnRegisterResponse;

public class MyFirebaseAuth {
    private static final String TAG = "MyFirebaseAuth";

    private FirebaseAuth mAuth;

    OnRegisterResponse onRegisterListener;
    OnLoginResponse onLoginResponse;

    public void registerToFirebase(String email, String password, OnRegisterResponse onRegisterListener){
        this.onRegisterListener = onRegisterListener;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
//                      Snackbar.make(requireContext(),requireView(),"Success to auth",Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "addAccountToFirebase: Success");
                        onRegisterListener.setOnRegisterResponse(true,"Success");
//                        //error here
//                        Navigation.findNavController(requireActivity(),R.id.action_registerFragment_to_loginFragment);

                    }else {
//                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show();
                        onRegisterListener.setOnRegisterResponse(false,task.getException().getLocalizedMessage());
                        Log.e(TAG, "addAccountToFirebase: failed"+task.getException().getLocalizedMessage());
//                      Snackbar.make(requireContext(),requireView(),"Failed to auth"+task.getException().getLocalizedMessage(),Snackbar.LENGTH_SHORT).show();

                    }
                });

    }


    public void signInToFirebase(String email, String password, OnLoginResponse onLoginResponse){
        this.onLoginResponse = onLoginResponse;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
//                      Snackbar.make(requireContext(),requireView(),"Success to auth",Snackbar.LENGTH_SHORT).show();
//                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "loginAccountWithFirebase: Success");
                        onLoginResponse.setOnLoginResponse(true,"Success");
//                        //error here
//                        Navigation.findNavController(requireActivity(),R.id.action_registerFragment_to_loginFragment);

                    }else {
//                        Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show();
                        onLoginResponse.setOnLoginResponse(false,task.getException().getLocalizedMessage());
                        Log.e(TAG, "loginAccountWithFirebase: failed"+task.getException().getLocalizedMessage());
//                      Snackbar.make(requireContext(),requireView(),"Failed to auth"+task.getException().getLocalizedMessage(),Snackbar.LENGTH_SHORT).show();

                    }
                });

    }

}
