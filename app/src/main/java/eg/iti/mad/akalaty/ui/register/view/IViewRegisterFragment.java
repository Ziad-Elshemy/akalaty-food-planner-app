package eg.iti.mad.akalaty.ui.register.view;

import eg.iti.mad.akalaty.model.AppUser;

public interface IViewRegisterFragment {




    public void showOnUserRegisterSuccessWithGoogle(AppUser appUser);
    public void showOnUserRegisterFailureWithGoogle(String errMsg);
}
