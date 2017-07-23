package ims.com.employee.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DravitLochan on 05-06-2017.
 */

public class IMEI {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "employee";
    private static final String IMEI_NUMBER = "imei_number";

    public IMEI(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public String getIMEINumber() {
        return sharedPreferences.getString(IMEI_NUMBER, null);
    }

    public void setImeiNumber(String ImeiNumber) {
        editor.putString(IMEI_NUMBER, ImeiNumber);
        editor.commit();
    }
}
