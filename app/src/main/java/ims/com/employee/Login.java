package ims.com.employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ims.com.employee.Helpers.InternetCheck;
import ims.com.employee.Models.User;
import ims.com.employee.prefs.IMEI;
import ims.com.employee.prefs.UserCreds;

public class Login extends AppCompatActivity {

    Button register, login;
    EditText login_email, login_password, login_user_name;
    IMEI imei;
    Context context;
    ProgressDialog progressDialog;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        auth=FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(context);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.open_signup);
        login_email = (EditText) findViewById(R.id.login_email);
        login_user_name = (EditText) findViewById(R.id.login_user_name);
        login_password = (EditText) findViewById(R.id.login_password);
        imei = new IMEI(context);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InternetCheck.internetCheck(context))
                {
                    progressDialog.setMessage("checking your details...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if(checkFeilds())
                    {
                        progressDialog.setMessage("signing you in...");
                        auth.signInWithEmailAndPassword(login_email.getText().toString(),login_password.getText().toString())
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            Toast.makeText(context,"have you signed up before?",Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
//                                        giveSignUpPrompt();
                                        }
                                        else {
                                            User user = new User(login_user_name.getText().toString(),login_email.getText().toString(),login_password.getText().toString());
                                            UserCreds userCreds = new UserCreds(context);
                                            userCreds.setIsUserSet(true);
                                            userCreds.setUser(user);
                                            mDatabaseReference = mFirebaseDatabase.getReference().child(login_user_name.getText().toString());
                                            mDatabaseReference.push().setValue("Device id : "+imei.getIMEINumber());
                                            Toast.makeText(context, imei.getIMEINumber(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            Toast.makeText(context,"welcome back!",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else
                        progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(context,"No Internet Connection!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Signup.class));
                finish();
            }
        });
    }

    public boolean checkFeilds() {
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        if(!login_email.getText().toString().contains("@imedisecure.com"))
        {
            login_email.setError("invalid email id!");
            return false;
        }
        if(login_password.getText().toString().length()<6)
        {
            login_password.setError("password should contain atleast 6 characters!");
            return  false;
        }
        if(login_password.getText().toString().contains(" "))
        {
            login_password.setError("cannot contain blank spaces");
            return false;
        }
        return true;
    }
}
