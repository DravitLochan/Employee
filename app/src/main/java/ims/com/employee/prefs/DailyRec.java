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
    private static final String IS_CHECKED_IN = "IsChecked";
    private static final String DATE = "date";
    private static final String CHECK_IN_TIME = "checkInTime";

    public DailyRec()
    {

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

    public String getCheckInTime()
    {
        return prefs.getString(CHECK_IN_TIME,null);
    }

    public void setCheckInTime(String checkInTime)
    {
        editor.putString(CHECK_IN_TIME,checkInTime);
        editor.commit();
    }
}
