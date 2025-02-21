package eg.iti.mad.akalaty.auth;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

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
                        Log.e(TAG, "addAccountToFirebase: Success");
                        onRegisterListener.setOnRegisterResponse(true,"Success");

                    }else {
                        onRegisterListener.setOnRegisterResponse(false,task.getException().getLocalizedMessage());
                        Log.e(TAG, "addAccountToFirebase: failed"+task.getException().getLocalizedMessage());

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

                        Log.e(TAG, "loginAccountWithFirebase: Success");
                        onLoginResponse.setOnLoginResponse(true,"Success");

                    }else {

                        onLoginResponse.setOnLoginResponse(false,task.getException().getLocalizedMessage());
                        Log.e(TAG, "loginAccountWithFirebase: failed"+task.getException().getLocalizedMessage());

                    }
                });

    }

}
