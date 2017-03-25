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

    int PRIVATE_MODE=0;
    private static final String PREF_NAME="employee";
    private static final String IS_CHECKED_IN = "IsChecked";

    public DailyRec(Context context)
    {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void setIsCheckedIn(boolean isCheckedIn)
    {
        editor.putBoolean(IS_CHECKED_IN, isCheckedIn);
        editor.commit();
    }

    public boolean getIsCheckedIn()
    {
        return prefs.getBoolean(IS_CHECKED_IN, false);
    }
}
