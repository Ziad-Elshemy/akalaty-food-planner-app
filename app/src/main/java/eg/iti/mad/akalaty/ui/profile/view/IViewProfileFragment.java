package eg.iti.mad.akalaty.ui.profile.view;

public interface IViewProfileFragment {
    public void showOnUploadSuccess(String msg);
    public void showOnUploadFailure(String msg);
    public void showOnDownloadSuccess(String msg);
    public void showOnDownloadFailure(String msg);
    public void showOnLogoutSuccess(String msg);
    public void showOnLogoutFailure(String msg);
}
