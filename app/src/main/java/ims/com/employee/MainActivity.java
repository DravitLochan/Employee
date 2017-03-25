package ims.com.employee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ims.com.employee.prefs.DailyRec;
import ims.com.employee.prefs.UserCreds;

public class MainActivity extends AppCompatActivity {

    Context context;
    UserCreds userCreds;
    int hours, min;
    Geocoder geocoder;
    LocationManager locationManager;
    LocationListener locationListener;
    Button check_in, check_out, update_me, sales_sheet;
    Location devLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sales_sheet = (Button) findViewById(R.id.button_sales_sheet);
        update_me = (Button) findViewById(R.id.update_me);
        check_in = (Button) findViewById(R.id.button_check_in);
        check_out = (Button) findViewById(R.id.button_check_out);
        geocoder = new Geocoder(this, Locale.getDefault());
        context = this;
        userCreds = new UserCreds(context);

        if (!userCreds.getIsUserSet()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
        else
        {
            Toast.makeText(context, "welcome " + userCreds.getUser().getEmail(), Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(getApplicationContext(), "called!", Toast.LENGTH_SHORT).show();
                /*
                timeUpdated.setText("time : "+sDate + "");
                */
                devLocation = location;

                try {
                    List<Address> add = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    //details.setText(add.get(0).getLocality()+"\n"+add.get(0).getAddressLine(0)+"\n"+add.get(0).getFeatureName()+"\n"+"\n"+add.get(0).getSubLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), "test123", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(devLocation==null)
                {
                    Toast.makeText(context,"please wait...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    long longD = devLocation.getTime();
                    Date d = new Date(longD);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                    String sDate = sdf.format(d);
                    if(!(d.getHours()>10&&d.getHours()<16))
                        Toast.makeText(context,"Office hours starts @10!",Toast.LENGTH_SHORT).show();
                    else
                    {
                        DailyRec dailyRec = new DailyRec(context);
                        if(dailyRec.getIsCheckedIn()==false)
                            Toast.makeText(context,"noted!",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context,"already checked in!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(devLocation==null)
                {
                    Toast.makeText(context,"please wait...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DailyRec dailyRec= new DailyRec(context);
                    if(dailyRec.getIsCheckedIn()==true)
                    {
                        long longD = devLocation.getTime();
                        Date d = new Date(longD);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                        String sDate = sdf.format(d);
                        if((d.getHours()>10&&d.getHours()<16))
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Confirm Check-out...");

                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure you want check-out?");

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    Toast.makeText(getApplicationContext(), "Checked out!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "it was a wise decision!", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                        else
                        {
                            getExp(context);
                        }
                    }
                    else
                    {
                        Toast.makeText(context,"you have not checked in yet",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        update_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"coming soon..!!",Toast.LENGTH_SHORT).show();
            }
        });
        sales_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "soon we will let you track your progress",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        Toast.makeText(context,"allow location access to the app!",Toast.LENGTH_SHORT).show();
    }

    public void getExp(final Context context)
    {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.expences, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                EditText exp = (EditText) promptsView.findViewById(R.id.exp);
                                DailyRec dailyRec = new DailyRec(context);
                                dailyRec.setIsCheckedIn(false);
                                Float.parseFloat(exp.getText().toString());
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}