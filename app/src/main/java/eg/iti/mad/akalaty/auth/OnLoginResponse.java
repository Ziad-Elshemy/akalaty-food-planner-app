package eg.iti.mad.akalaty.auth;

public interface OnLoginResponse {
    public void setOnLoginSuccess(String userId);
    public void setOnLoginFailure(String msg);
}
