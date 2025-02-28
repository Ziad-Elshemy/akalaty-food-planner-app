package eg.iti.mad.akalaty.ui.register.presenter;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;

import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executors;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.auth.MyFirebaseAuth;
import eg.iti.mad.akalaty.repo.MealsRepo;
import eg.iti.mad.akalaty.ui.login.view.IViewLoginFragment;
import eg.iti.mad.akalaty.ui.register.view.IViewRegisterFragment;

public class RegisterPresenter  implements IRegisterPresenter{


    private static final String TAG = "RegisterPresenter";
    IViewRegisterFragment _view;
    MealsRepo _repo;
    MyFirebaseAuth mAuth;


    public RegisterPresenter(IViewRegisterFragment _view, MealsRepo _repo){
        this._view = _view;
        this._repo = _repo;
        mAuth = new MyFirebaseAuth();
    }

    @Override
    public void signUpWithGoogle(Context context, CredentialManager credentialManager) {
        launchSignUpCredentialManager(context,credentialManager);
    }

    public void launchSignUpCredentialManager(Context context, CredentialManager credentialManager) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignUp(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        if (e.getLocalizedMessage().contains("Cannot find a matching credential")) {
//                            loginView.navigateToSignUp();
                            _view.showOnUserRegisterFailureWithGoogle("Cannot find a matching credential");
                        } else {
//                            loginView.showError("Error signing in. Please try again.");
                            _view.showOnUserRegisterFailureWithGoogle("Error signing in. Please try again.");
                        }
                    }
                }
        );
    }

    private void handleSignUp(Credential credential) {
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (customCredential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                mAuth.firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), _view);
            } else {
//                loginView.showError("Credential is not of type Google ID!");
                _view.showOnUserRegisterFailureWithGoogle("Credential is not of type Google ID!");
            }
        }
    }
}
