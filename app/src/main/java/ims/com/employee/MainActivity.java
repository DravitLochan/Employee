package ims.com.employee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
import ims.com.employee.Models.DailyRecord;
import ims.com.employee.Models.EmployeeCheckInModel;
import ims.com.employee.Models.LocationDets;
import ims.com.employee.prefs.CheckIn;
import ims.com.employee.prefs.DailyRec;
import ims.com.employee.prefs.IMEI;
import ims.com.employee.prefs.UserCreds;

import static android.provider.Settings.Secure.ANDROID_ID;

public class MainActivity extends AppCompatActivity {

    private static int permReqCode = 111, premCodePhoneState = 110;
    Context context;
    UserCreds userCreds;
    IMEI imei;
    Geocoder geocoder;
    LocationManager locationManager;
    LocationListener locationListener;
    Button check_in, check_out, track_status, sales_sheet;
    Location devLocation;
    String nameOfLocation, timeStamp;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, premCodePhoneState);
        }
        sales_sheet = (Button) findViewById(R.id.button_sales_sheet);
        track_status = (Button) findViewById(R.id.track_status);
        check_in = (Button) findViewById(R.id.button_check_in);
        check_out = (Button) findViewById(R.id.button_check_out);
        geocoder = new Geocoder(this, Locale.getDefault());
        context = this;
        userCreds = new UserCreds(context);
        imei = new IMEI(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(userCreds.getUser().getUser_name() + "");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //Toast.makeText(context, uid, Toast.LENGTH_SHORT).show();
        imei.setImeiNumber(uid);


        if (!userCreds.getIsUserSet()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        } else {
            Toast.makeText(context, "welcome " + userCreds.getUser().getEmail(), Toast.LENGTH_SHORT).show();
            DailyRec dailyRec = new DailyRec(context);
            if (dailyRec.getDayStarted() == true) {
                track_status.setText("end day");
            } else {
                track_status.setText("start day");
            }
        }
        locationManager = (LocationManager) getSystemService(context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("location", location.toString());
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
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                return;
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.internetCheck(context) == false) {
                    Toast.makeText(context, "No Internet Available!", Toast.LENGTH_SHORT).show();
                } else {
                    final DailyRec dailyRec = new DailyRec(context);
                    if (dailyRec.getIsCheckedIn() == false) {
                        if (dailyRec.getDayStarted() == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.description, null);
                            builder.setView(dialogView);
                            final AlertDialog alertDialog = builder.create();


                            if (devLocation == null) {
                                Toast.makeText(context, "please wait...", Toast.LENGTH_SHORT).show();
                            } else {
                                alertDialog.show();
                                final EditText location = (EditText) dialogView.findViewById(R.id.name_of_location);
                                final EditText description = (EditText) dialogView.findViewById(R.id.descripiton);
                                final Button submitButton = (Button) dialogView.findViewById(R.id.submit_button);
                                submitButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            if (location.getText().toString().length() == 0 || location.getText().toString().equals(" ")) {
                                                Toast.makeText(context, "can't be left blank. Please try again", Toast.LENGTH_SHORT).show();
                                            } else if (description.getText().toString().length() == 0 || description.getText().toString().equals(" ")) {
                                                Toast.makeText(context, "can't be left blank. Please try again", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ProgressDialog progressDialog = new ProgressDialog(context);
                                                progressDialog.setMessage("Saving your details...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                //EmployeeCheckInModel employeeCheckInModel = new EmployeeCheckInModel();
                                                //employeeCheckInModel.setIn_location(location.getText().toString());                                         //1
                                                //employeeCheckInModel.setIn_description(description.getText().toString());                                   //2
                                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                                List<Address> add = geocoder.getFromLocation(devLocation.getLatitude(), devLocation.getLongitude(), 1);
                                                //employeeCheckInModel.setIn_loc_details(add.get(0).getAddressLine(0) + " " + add.get(0).getLocality());          //3
                                                //employeeCheckInModel.setIn_time(timeStamp);                                                                 //4

                                        /*EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(getBaseContext());
                                        int id = db.insertCheckInDetails(employeeCheckInModel);
                                        dailyRec.setCheckInId(id);*/
                                                dailyRec.setIsCheckedIn(true);
                                                CheckIn checkIn = new CheckIn(context);
                                                checkIn.setCheckInDescription(description.getText().toString());
                                                checkIn.setCheckInLocation(location.getText().toString());
                                                checkIn.setCheckInDetails(add.get(0).getAddressLine(0) + " " + add.get(0).getSubLocality() + " " + add.get(0).getLocality());
                                                Log.i("sub locality", "" + add.get(0).getSubLocality());
                                                checkIn.setCheckInTime(timeStamp);
                                                progressDialog.dismiss();
                                                Toast.makeText(getBaseContext(), "Checked In Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (IOException e) {
                                            Toast.makeText(context, "Some error occured! Please restart the app!", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(context, "Please start the day before checking in!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Already checked in", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.internetCheck(context) == false) {
                    Toast.makeText(context, "No Internet Available!", Toast.LENGTH_SHORT).show();
                } else {
                    final DailyRec dailyRec = new DailyRec(context);
                    if (dailyRec.getIsCheckedIn() == true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.checkout_description, null);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();

                        if (devLocation == null) {
                            Toast.makeText(context, "please wait...", Toast.LENGTH_SHORT).show();
                        } else {
                            alertDialog.show();
                            final EditText description = (EditText) dialogView.findViewById(R.id.check_out_descripiton);
                            final Button submitButton = (Button) dialogView.findViewById(R.id.check_out_submit_button);
                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            submitButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        if (description.getText().toString().equals(" ") || description.getText().toString().length() == 0) {
                                            Toast.makeText(context, "can't be left blank", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.setMessage("Please Wait...");
                                            progressDialog.setCancelable(false);
                                            progressDialog.show();
                                            //EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(getBaseContext());
                                            EmployeeCheckInModel employee = new EmployeeCheckInModel();
                                            Log.i("checked in employee", employee + "");
                                            CheckIn checkIn = new CheckIn(context);
                                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                            List<Address> add = null;
                                            add = geocoder.getFromLocation(devLocation.getLatitude(), devLocation.getLongitude(), 1);

                                            //here extracted values from preferences and made the object

                                            employee.setIn_location(checkIn.getCheckInLocation());
                                            employee.setIn_loc_details(checkIn.getCheckInDetails());
                                            employee.setIn_description(checkIn.getCheckInDescription());
                                            employee.setIn_time(checkIn.getCheckInTime());

                                            //enter the check out values
                                            employee.setOut_description(description.getText().toString());                                      //1
                                            employee.setOut_time(timeStamp);                                                                    //2
                                            employee.setOut_loc_details(add.get(0).getAddressLine(0) + " " + add.get(0).getSubLocality() + " " + add.get(0).getLocality());              //3
                                            employee.setOut_location(employee.getIn_location());                                                 //4

                                            databaseReference.push().setValue(employee);
                                            progressDialog.dismiss();
                                            //db.updateToCompleteEntry(dailyRec.getCheckInId(), employee);
                                            Toast.makeText(getBaseContext(), "Checked Out Successfully", Toast.LENGTH_SHORT).show();
                                            dailyRec.setIsCheckedIn(false);
                                        }
                                    } catch (IOException e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Some error Occured! Please restart the app", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    //Todo give a prompt to set alarm to the user
                                    Toast.makeText(getBaseContext(), "Checked Out Successfully", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });

                        }
                    } else {
                        Toast.makeText(context, "You must check in first", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        track_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.internetCheck(context) == false) {
                    Toast.makeText(context, "No Internet Available!", Toast.LENGTH_SHORT).show();
                } else {
                    if (devLocation == null) {
                        Toast.makeText(context, "Please Wait...", Toast.LENGTH_SHORT).show();
                    } else {
                        DailyRec dailyRec = new DailyRec(context);
                        long longD = devLocation.getTime();
                        Date d = new Date(longD);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy HH:mm:ss");
                        final String sDate = sdf.format(d);
                        if (dailyRec.getDayStarted() == false) {
                            if ((d.getHours() >= 0 && d.getHours() < 24)) {
                                DailyRec dailyRec1 = new DailyRec(context);
                                dailyRec1.setDayStarted(true);
                                track_status.setText("end day");
                                Toast.makeText(context, "Go Go Go!!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "office starts at 10!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (dailyRec.getIsCheckedIn() == false) {
                                if (d.getHours() > 0 && d.getHours() < 24) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                    // Setting Dialog Title
                                    alertDialog.setTitle("Confirm Check-out...");
                                    // Setting Dialog Message
                                    alertDialog.setMessage("Are you sure you want check-out?");
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            DailyRec dailyRec1 = new DailyRec(context);
                                            dailyRec1.setDayStarted(false);
                                            track_status.setText("start day");
                                            Toast.makeText(getApplicationContext(), "Day has Ended!", Toast.LENGTH_SHORT).show();
                                            //getExp(context);
                                            Toast.makeText(context, "Hope you had a good day!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(context, "Go on. grab the stuff you forgot!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.show();
                                } else {
                                    //getExp(context);
                                    Toast.makeText(context, "Hope you had a good day!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "checked out from the last location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        sales_sheet.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Soon we will start you track your record", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, SalesSheet.class));
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
            DailyRec dailyRec = new DailyRec(context);
            dailyRec.setIsCheckedIn(false);
            dailyRec.setDayStarted(false);
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        timeStamp = new SimpleDateFormat("dd:MM:yy HH:mm:ss").format(Calendar.getInstance().getTime());
        if (devLocation == null) {
            Toast.makeText(context, "Please switch on the location", Toast.LENGTH_SHORT).show();
        }
    }

}
