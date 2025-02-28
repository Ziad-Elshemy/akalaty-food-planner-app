package eg.iti.mad.akalaty.ui.register.presenter;

import android.content.Context;

import androidx.credentials.CredentialManager;

public interface IRegisterPresenter {



    public void signUpWithGoogle(Context context, CredentialManager credentialManager);
}
