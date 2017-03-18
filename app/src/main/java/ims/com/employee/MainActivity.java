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

        final Button b=(Button)findViewById(R.id.button2) ;
        final TextView TextV=(TextView)findViewById(R.id.textView);
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


            final Calendar c = Calendar.getInstance();
            long millis=System.currentTimeMillis();
            c.setTimeInMillis(millis);
            hours=c.get(Calendar.HOUR_OF_DAY);
            min=c.get(Calendar.MINUTE);
            TextV.setText(hours+":"+min);

            b.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            long millis=System.currentTimeMillis();
                            c.setTimeInMillis(millis);
                            hours=c.get(Calendar.HOUR_OF_DAY);
                            min=c.get(Calendar.MINUTE);
                            TextV.setText(hours+":"+min);
//if button displaying check in --------------------------------------------------------------
                            if(b.getText().toString().equals("CHECK IN")){

                                if (hours>=10&&hours<16) {
                                    b.setText("CHECK OUT");
                                }
                                else
                                    Toast.makeText(context,"office hours are 10:00-4:00",Toast.LENGTH_SHORT).show();

                            }
//if button displaying check out --------------------------------------------------------------
                            else {
                                if (hours >=16)
                                b.setText("CHECK IN");
                                else {
                                    altr.setMessage("Bhai pkka jara h?").setCancelable(false)
                                    .setPositiveButton("hn yr ", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            b.setText("CHECK IN");
                                        }
                                    })
                                    .setNegativeButton("soch to ra ta yr", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alert=altr.create();
                                    alert.setTitle("pese katenge");
                                    alert.show();
                                }

                            }
                        }
                    }
            );

        }
    }

}