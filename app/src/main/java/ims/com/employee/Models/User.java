package ims.com.employee.Models;

/**
 * Created by DravitLochan on 16-03-2017.
 */

public class User {
    String email,password,user_name;

    public User()
    {

    }

    public User(String user_name, String email, String password)
    {
        this.user_name = user_name;
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

    public String getUser_name()
    {
        return user_name;
    }
}
