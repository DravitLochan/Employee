package ims.com.employee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ims.com.employee.prefs.UserCreds;

public class MainActivity extends AppCompatActivity {

    Context context;
    UserCreds userCreds;
    int hours,min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertDialog.Builder altr=new AlertDialog.Builder(MainActivity.this);

        context=this;
        userCreds = new UserCreds(context);
        if(!userCreds.getIsUserSet())
        {
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }

        else
        {
            Toast.makeText(context,"welcome "+userCreds.getUser().getEmail(),Toast.LENGTH_SHORT).show();

        }
    }

}