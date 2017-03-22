package ims.com.employee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ims.com.employee.prefs.UserCreds;

public class MainActivity extends AppCompatActivity {

    Context context;
    UserCreds userCreds;
    int hours,min;
    LocationListener LocLis;
    Geocoder geocoder;
    LocationManager LocMan;
    Button Update_Bttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Update_Bttn=(Button)findViewById(R.id.update_me);

        geocoder=new Geocoder(this, Locale.getDefault());
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


        Update_Bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Test onClick",Toast.LENGTH_LONG).show();

                LocMan=(LocationManager)getSystemService(LOCATION_SERVICE);

                LocLis=new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(getApplicationContext(),"called!",Toast.LENGTH_SHORT).show();
                        long longD = location.getTime();
                        Date d = new Date(longD);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                        String sDate = sdf.format(d);
                        try {
                            List<Address> add= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), sDate+""+longD , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
            }
        });




    }

}