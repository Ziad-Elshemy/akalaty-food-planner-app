package eg.iti.mad.akalaty.ui.login.presenter;

import android.content.Context;

import androidx.credentials.CredentialManager;

import eg.iti.mad.akalaty.auth.OnLoginResponse;

public interface ILoginPresenter {
    public void downloadDataFromFirestore(String userId);
    public void checkUserFromFirestore(String userId);
    public void addAccountToFirebase(String email, String password, OnLoginResponse onLoginResponse);
    public void signInWithGoogle(Context context, CredentialManager credentialManager);
}
