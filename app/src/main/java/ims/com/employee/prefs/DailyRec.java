package ims.com.employee.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DravitLochan on 25-03-2017.
 */

public class DailyRec {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "employee";
    private static final String DAY_STARTED = "dayStarted";
    private static final String DATE = "date";
    private static final String CHECK_IN_ID = "checkInId";

    private static final String IS_CHECKED_IN = "IsChecked";

    public DailyRec() {

    }

    public DailyRec(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void setIsCheckedIn(boolean isCheckedIn) {
        editor.putBoolean(IS_CHECKED_IN, isCheckedIn);
        editor.commit();
    }

    public boolean getIsCheckedIn() {
        return prefs.getBoolean(IS_CHECKED_IN, false);
    }

    public int getDate() {
        return prefs.getInt(DATE, 1);
    }

    public void setDate(int date) {
        editor.putInt(DATE, date);
        editor.commit();
    }

    
    public Boolean getDayStarted() {
        return prefs.getBoolean(DAY_STARTED, false);
    }

    public void setDayStarted(boolean value) {
        editor.putBoolean(DAY_STARTED, value);
        editor.commit();
    }

    public int getCheckInId() {
        return prefs.getInt(CHECK_IN_ID, 1);
    }

    public void setCheckInId(int id) {
        editor.putInt(CHECK_IN_ID, id);
        editor.commit();
    }
}
