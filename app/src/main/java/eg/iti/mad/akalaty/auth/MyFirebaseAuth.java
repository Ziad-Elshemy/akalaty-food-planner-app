package eg.iti.mad.akalaty.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import eg.iti.mad.akalaty.auth.firestore.FirestoreUtils;
import eg.iti.mad.akalaty.model.AppUser;
import eg.iti.mad.akalaty.ui.login.view.IViewLoginFragment;
import eg.iti.mad.akalaty.ui.register.view.IViewRegisterFragment;

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
                        createFirestoreUser(task.getResult().getUser().getUid(),username,email,onRegisterListener);

                    }else {
                        Log.e(TAG, "addAccountToFirebase: failed"+task.getException().getLocalizedMessage());
                        onRegisterListener.setOnRegisterResponse(false,task.getException().getLocalizedMessage());
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

    public void firebaseAuthSignInWithGoogle(String idToken, OnLoginResponse onLoginResponse) {
        this.onLoginResponse = onLoginResponse;
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        AppUser appUser = new AppUser(user.getUid(),user.getDisplayName(),user.getEmail());
//                        iViewLoginFragment.showOnUserLoginSuccessWithGoogle(appUser);
                        Log.e(TAG, "loginAccountWithFirebase: Success");
                        onLoginResponse.setOnLoginSuccess(task.getResult().getUser().getUid());
                    } else {
                        onLoginResponse.setOnLoginFailure(task.getException().getLocalizedMessage());
                        Log.e(TAG, "loginAccountWithFirebase: failed"+task.getException().getLocalizedMessage());
//                        iViewLoginFragment.showOnUserLoginFailure("Authentication failed: " + task.getException().getMessage());
                    }
                });
    }
    public void firebaseAuthSignUpWithGoogle(String idToken, OnRegisterResponse onRegisterResponse) {
        this.onRegisterListener = onRegisterResponse;
        mAuth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "addAccountToFirebase: Success");
                        createFirestoreUser(task.getResult().getUser().getUid(),task.getResult().getUser().getDisplayName(),task.getResult().getUser().getEmail(),onRegisterListener);

                    } else {
                        Log.e(TAG, "addAccountToFirebase: failed"+task.getException().getLocalizedMessage());
                        onRegisterListener.setOnRegisterResponse(false,task.getException().getLocalizedMessage());
                    }
                });
    }


}
