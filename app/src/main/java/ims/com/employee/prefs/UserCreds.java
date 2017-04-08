package ims.com.employee.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import ims.com.employee.Models.User;

/**
 * Created by DravitLochan on 16-03-2017.
 */

public class UserCreds {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE=0;
    private static final String PREF_NAME="employee";
    private static final String EMAIL="email";
    private static final String USER_NAME = "UserName";
    private static final String PASSWORD="password";
    private static final String IS_USER_SET="IsUserSet";

    public UserCreds(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void setUser(User user)
    {
        editor.putString(EMAIL,user.getEmail());
        editor.putString(PASSWORD,user.getPassword());
        editor.putString(USER_NAME,user.getUser_name());
        editor.commit();
    }

    public User getUser()
    {
        User user = new User(sharedPreferences.getString(USER_NAME,null),sharedPreferences.getString(EMAIL,null),sharedPreferences.getString(PASSWORD,null));
        return user;
    }

    public boolean getIsUserSet()
    {
        return sharedPreferences.getBoolean(IS_USER_SET,false);
    }

    public void setIsUserSet(boolean value)
    {
        editor.putBoolean(IS_USER_SET,value);
        editor.commit();
    }
}
