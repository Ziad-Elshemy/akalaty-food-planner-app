package eg.iti.mad.akalaty.ui.login.presenter;

public interface ILoginPresenter {
    public void downloadDataFromFirestore(String userId);
    public void checkUserFromFirestore(String userId);
}
