package ims.com.employee.Models;

/**
 * Created by DravitLochan on 16-03-2017.
 */

public class User {
    String email,password,name;

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
}
