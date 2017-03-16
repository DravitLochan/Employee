package ims.com.employee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ims.com.employee.prefs.UserCreds;

public class MainActivity extends AppCompatActivity {

    Context context;
    UserCreds userCreds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        userCreds = new UserCreds(context);

        if(!userCreds.getIsUserSet())
        {
            startActivity(new Intent(MainActivity.this,Login.class));
        }
        else
        {
            Toast.makeText(context,"welcome "+userCreds.getUser().getEmail(),Toast.LENGTH_SHORT).show();
        }
    }
}
