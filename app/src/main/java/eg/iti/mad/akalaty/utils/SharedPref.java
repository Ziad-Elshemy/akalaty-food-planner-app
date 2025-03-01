package eg.iti.mad.akalaty.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {


    private static Context _context;
    private static final String SHARED_PREFERENCES_NAME = "user_pref";
    private static final String USER_ID = "id";
    private static final String USER_NAME = "username";
    private static final String USER_EMAIL = "email";
    private static final String IS_LOGGED = "isLogged";
    private static final String IS_FINISHED = "isFinished";
    private static final String IS_SAME_DAY = "isSameDay";
    private static final String MEAL_DATE = "mealDate";
    private static final String MEAL_ID = "mealId";
    private SharedPreferences sharedPreferences;
    private static SharedPref sharedPref = null;

    private SharedPref(Context context){
        _context = context;
        sharedPreferences = _context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance(Context context) {
        if (sharedPref==null){
            sharedPref = new SharedPref(context);
        }
        return sharedPref;
    }

    public void setIsFinished(boolean isFinished) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FINISHED, isFinished).apply();
    }

    public boolean getIsFinished() {
        return sharedPreferences.getBoolean(IS_FINISHED, false);
    }


    public void setIsLogged(boolean isLogged) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED, isLogged).apply();
    }

    public boolean getIsLogged() {
        return sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }
    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_EMAIL, userEmail).apply();
    }


    public void setUserId(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userEmail).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }


    public void setIsSameDay(boolean isSameDay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAME_DAY, isSameDay).apply();
    }

    public boolean getIsSameDay() {
        return sharedPreferences.getBoolean(IS_SAME_DAY, false);
    }

    public void setMealDate(Long mealDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(MEAL_DATE, mealDate).apply();
    }

    public Long getMealDate() {
        return sharedPreferences.getLong(MEAL_DATE, 0L);
    }

    public void setMealId(String mealId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MEAL_ID, mealId).apply();
    }

    public String getMealId() {
        return sharedPreferences.getString(MEAL_ID, "");
    }


}
