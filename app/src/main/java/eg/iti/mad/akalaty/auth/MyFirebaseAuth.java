package eg.iti.mad.akalaty.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import eg.iti.mad.akalaty.auth.firestore.FirestoreUtils;
import eg.iti.mad.akalaty.model.AppUser;

public class MyFirebaseAuth {
    private static final String TAG = "MyFirebaseAuth";

    private FirebaseAuth mAuth;

    OnRegisterResponse onRegisterListener;
    OnLoginResponse onLoginResponse;

    public void registerToFirebase(String email, String username, String password, OnRegisterResponse onRegisterListener){
        this.onRegisterListener = onRegisterListener;
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.e(TAG, "addAccountToFirebase: Success");
//                        onRegisterListener.setOnRegisterResponse(true,"Success");
                        createFirestoreUser(task.getResult().getUser().getUid(),username,email,onRegisterListener);

                    }else {
//                        onRegisterListener.setOnRegisterResponse(false,task.getException().getLocalizedMessage());
                        Log.e(TAG, "addAccountToFirebase: failed"+task.getException().getLocalizedMessage());

                    }
                });

    }

    private void createFirestoreUser(String userId, String username, String email, OnRegisterResponse onRegisterListener) {
        AppUser user = new AppUser(userId, username, email);
        FirestoreUtils.addUserToFirestore(user,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "onSuccess: createFirestoreUser");
                        onRegisterListener.setOnRegisterResponse(true,"Success");
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: createFirestoreUser");
                        onRegisterListener.setOnRegisterResponse(false,e.getLocalizedMessage());
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
                        onLoginResponse.setOnLoginSuccess(task.getResult().getUser().getUid());

                    }else {

                        onLoginResponse.setOnLoginFailure(task.getException().getLocalizedMessage());
                        Log.e(TAG, "loginAccountWithFirebase: failed"+task.getException().getLocalizedMessage());

                    }
                });

    }

}
