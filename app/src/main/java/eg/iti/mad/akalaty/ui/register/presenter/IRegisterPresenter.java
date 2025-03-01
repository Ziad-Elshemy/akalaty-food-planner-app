package eg.iti.mad.akalaty.ui.register.presenter;

import android.content.Context;

import androidx.credentials.CredentialManager;

import eg.iti.mad.akalaty.auth.OnRegisterResponse;

public interface IRegisterPresenter {



    public void signUpWithGoogle(Context context, CredentialManager credentialManager, OnRegisterResponse onRegisterResponse);
}
