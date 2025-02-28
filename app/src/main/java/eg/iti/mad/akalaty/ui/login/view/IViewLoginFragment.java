package eg.iti.mad.akalaty.ui.login.view;

import eg.iti.mad.akalaty.model.AppUser;

public interface IViewLoginFragment {
    public void showOnDataFetchedFromFirestore();
    public void showOnUserLoginSuccess(AppUser appUser);
    public void showOnUserLoginFailure(String errMsg);
}
