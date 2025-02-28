package eg.iti.mad.akalaty.ui.profile.presenter;

public interface IProfilePresenter {
    public void uploadDataToFirestore(String userId);
    public void downloadDataFromFirestore(String userId);
    public void logout();
}
