package ims.com.employee.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DravitLochan on 26-04-2017.
 */

public class CheckIn {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "employee";
    private static final String IS_CHECKED_IN = "IsChecked";
    private static final String CHECK_IN_TIME = "checkInTime";
    private static final String CHECK_IN_LOCATION = "checkInLocation";
    private static final String CHECK_IN_DESCRIPTION = "checkInDescription";
    private static final String CHECK_IN_DETAILS = "checkInDetails";
/*
    private static final String CHECK_OUT_DESCRIPTION = "checkOutDescription";
    private static final String CHECK_OUT_TIME = "checkOutTime";
    private static final String CHECK_OUT_LOCATION = "checkOutLocation";
    private static final String CHECK_OUT_DETAILS = "checkOutDetails";
*/
    public CheckIn(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public Boolean getIsCheckedIn() {
        return prefs.getBoolean(IS_CHECKED_IN, false);
    }

    public String getCheckInTime() {
        return prefs.getString(CHECK_IN_TIME, null);
    }

    public String getCheckInLocation() {
        return prefs.getString(CHECK_IN_LOCATION, null);
    }

    public String getCheckInDescription() {
        return prefs.getString(CHECK_IN_DESCRIPTION, null);
    }

    public String getCheckInDetails() {
        return prefs.getString(CHECK_IN_DETAILS, null);
    }
/*
    public String getCheckOutDescription() {
        return prefs.getString(CHECK_OUT_DESCRIPTION, null);
    }

    public String getCheckOutTime() {
        return prefs.getString(CHECK_OUT_TIME, null);
    }

    public String getCheckOutLocation() {
        return prefs.getString(CHECK_OUT_LOCATION, null);
    }

    public String getCheckOutDetails() {
        return prefs.getString(CHECK_OUT_DETAILS, null);
    }
    public void setIsCheckedIn(Boolean value) {
         editor.putBoolean(IS_CHECKED_IN, value);
        editor.commit();
    }
*/
    public void setCheckInTime(String time) {
        editor.putString(CHECK_IN_TIME, time);
        editor.commit();
    }

    public void setCheckInLocation(String location) {
        editor.putString(CHECK_IN_LOCATION, location);
        editor.commit();
    }

    public void setCheckInDescription(String description) {
        editor.putString(CHECK_IN_DESCRIPTION, description);
        editor.commit();
    }

    public void setCheckInDetails(String details) {
        editor.putString(CHECK_IN_DETAILS, details);
        editor.commit();
    }

    /*public void setCheckOutDescription() {
        return prefs.getString(CHECK_OUT_DESCRIPTION, null);
    }

    public String getCheckOutTime() {
        return prefs.getString(CHECK_OUT_TIME, null);
    }

    public String getCheckOutLocation() {
        return prefs.getString(CHECK_OUT_LOCATION, null);
    }

    public String getCheckOutDetails() {
        return prefs.getString(CHECK_OUT_DETAILS, null);
    }
    */
}
