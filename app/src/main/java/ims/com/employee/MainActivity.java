package ims.com.employee;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ims.com.employee.DatabaseHelper.EmployeeDatabaseHelper;
import ims.com.employee.Helpers.InternetCheck;
import ims.com.employee.Models.EmployeeCheckInModel;
import ims.com.employee.Models.LocationDets;
import ims.com.employee.prefs.DailyRec;
import ims.com.employee.prefs.UserCreds;

public class MainActivity extends AppCompatActivity {

    private static int permReqCode = 111;
    Context context;
    UserCreds userCreds;
    Geocoder geocoder;
    LocationManager locationManager;
    LocationListener locationListener;
    Button check_in, check_out, track_status, sales_sheet;
    Location devLocation;
    String nameOfLocation;
    Float exp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permReqCode);
        }
        sales_sheet = (Button) findViewById(R.id.button_sales_sheet);
        track_status = (Button) findViewById(R.id.track_status);
        check_in = (Button) findViewById(R.id.button_check_in);
        check_out = (Button) findViewById(R.id.button_check_out);
        geocoder = new Geocoder(this, Locale.getDefault());
        context = this;
        userCreds = new UserCreds(context);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(userCreds.getUser().getUser_name() + "");

        if (!userCreds.getIsUserSet()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else {
            Toast.makeText(context, "welcome " + userCreds.getUser().getEmail(), Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                devLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(context, "switch on the location", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("GPS not enabled");
                builder.setMessage("open and enable location via gps");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                return;
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("GPS not found");
                Toast.makeText(context, "switch on the location", Toast.LENGTH_SHORT).show();
                builder.setMessage("open and enable location via gps");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                return;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.description, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                check_in.setEnabled(Boolean.FALSE);
                check_out.setEnabled(Boolean.TRUE);

                if (devLocation == null) {
                    Toast.makeText(context, "please wait...", Toast.LENGTH_SHORT).show();
                }
                else{
                    alertDialog.show();
                    final EditText location = (EditText) dialogView.findViewById(R.id.name_of_location);
                    final EditText description = (EditText) dialogView.findViewById(R.id.descripiton);
                    final Button submitButton = (Button) dialogView.findViewById(R.id.submit_button);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String timeStamp = new SimpleDateFormat("dd:MM:yy HH:mm:ss").format(Calendar.getInstance().getTime());
                            EmployeeCheckInModel employeeCheckInModel = new EmployeeCheckInModel();
                            employeeCheckInModel.setLocation(location.getText().toString());
                            employeeCheckInModel.setDescription(description.getText().toString());
                            employeeCheckInModel.setLoc_details(devLocation.toString());
                            employeeCheckInModel.setCheck_in_time(timeStamp);
                            EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(getBaseContext());
                            db.insertNewCheckInDetails(employeeCheckInModel);
                            Toast.makeText(getBaseContext(), "Checked In Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });


        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_in.setEnabled(Boolean.TRUE);
                check_out.setEnabled(Boolean.FALSE);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.checkout_description, null);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                if (devLocation == null) {
                    Toast.makeText(context, "please wait...", Toast.LENGTH_SHORT).show();
                } else {
/*
                    //here there maybe a bug because it is declared final
                    final DailyRec dailyRec = new DailyRec(context);
                    if (dailyRec.getIsCheckedIn() == true) {
                        long longD = devLocation.getTime();
                        Date d = new Date(longD);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                        final String sDate = sdf.format(d);
                        if ((d.getHours() >= 10 && d.getHours() < 16)) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Confirm Check-out...");

                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure you want check-out?");

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DailyRec dailyRecSetCheckedIn = new DailyRec(context);
                                    dailyRecSetCheckedIn.setIsCheckedIn(false);
                                    Toast.makeText(getApplicationContext(), "Checked out!", Toast.LENGTH_SHORT).show();
                                    getExp(context);
                                    DailyRecord dailyRecord = new DailyRecord(dailyRec.getCheckInTime(), sDate, exp);
                                    databaseReference.push().setValue(dailyRecord);
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
                        } else {
                            getExp(context);
                            try {
                                DailyRecord dailyRecord = new DailyRecord(dailyRec.getCheckInTime(), sDate, exp);
                                databaseReference.push().setValue(dailyRecord);
                            } catch (Exception e) {
                                Toast.makeText(context, "error occured! please report to admin", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, "you have not checked in yet", Toast.LENGTH_SHORT).show();
                    }
*/
                    alertDialog.show();
                    final EditText description = (EditText) dialogView.findViewById(R.id.check_out_descripiton);
                    final Button submitButton = (Button) dialogView.findViewById(R.id.check_out_submit_button);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String timeStamp = new SimpleDateFormat("dd:MM:yy HH:mm:ss").format(Calendar.getInstance().getTime());
                            EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(getBaseContext());
                            EmployeeCheckInModel employee = db.getCheckInDetails();
                            employee.setCheck_out_description(description.getText().toString());
                            employee.setCheck_out_time(timeStamp);
                            //Todo
                            employee.setReminder("");
                            DatabaseReference ref = databaseReference.child("Check In Details");
                            ref.push().setValue(employee);
                            db.deleteCheckInDetails();
                            Toast.makeText(getBaseContext(), "Checked Out Successfully", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });


                }
            }
        });
        track_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyRec dailyRec = new DailyRec(context);
                if (dailyRec.getIsCheckedIn() == true) {
                    try {
                        //details.setText(add.get(0).getLocality()+"\n"+add.get(0).getAddressLine(0)+"\n"+add.get(0).getFeatureName()+"\n"+"\n"+add.get(0).getSubLocality());
                        if (InternetCheck.internetCheck(context) == true) {
                            List<Address> add = geocoder.getFromLocation(devLocation.getLatitude(), devLocation.getLongitude(), 1);
                            long longD = devLocation.getTime();
                            Date d = new Date(longD);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                            String sDate = sdf.format(d);
                            LocationDets locationDets = new LocationDets(nameOfLocation,
                                    add.get(0).getAddressLine(0) + " " + add.get(0).getSubLocality() + " " + add.get(0).getLocality(),
                                    sDate.substring(0, 9), sDate.substring(9, 17));
                            try {
                                databaseReference.push().setValue(locationDets);
                            } catch (Exception e) {
                                Toast.makeText(context, "error occured", Toast.LENGTH_SHORT).show();
                                Log.d("update push", "" + e.getMessage().toString());
                            }
                            Toast.makeText(context, "updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No Internet Access!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(context, "Some error occured. Please restart the application", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "you need to check-in first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sales_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "soon we will let you track your progress", Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        //Toast.makeText(context,"allow location access to the app!",Toast.LENGTH_SHORT).show();
    }*/

    public void getExp(final Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.expences, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText et_exp = (EditText) promptsView.findViewById(R.id.et_exp);
                                DailyRec dailyRec = new DailyRec(context);
                                dailyRec.setIsCheckedIn(false);
                                if (!(et_exp.getText().toString().equals("") || et_exp.getText().toString().contains(" "))) {
                                    exp = Float.parseFloat(et_exp.getText().toString());
                                    //ToDo: send this value to database
                                    Toast.makeText(context, "Hope you had a nice day!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "enter a valid amount!", Toast.LENGTH_SHORT).show();
                                    getExp(context);
                                }
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            UserCreds userCreds = new UserCreds(context);
            userCreds.setIsUserSet(false);
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}