package ims.com.employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import ims.com.employee.prefs.UserCreds;

public class Signup extends AppCompatActivity {

    EditText signup_email,signup_password,conf_password;
    Button signup,open_login;
    private Context context;
    private ProgressDialog progressDialog;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_password = (EditText) findViewById(R.id.signup_password);
        conf_password = (EditText) findViewById(R.id.conf_password);
        signup = (Button) findViewById(R.id.signup);
        progressDialog = new ProgressDialog(context);
        open_login = (Button) findViewById(R.id.open_login);

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.internetCheck(context))
                {
                    progressDialog.setMessage("Please Wait. Verifying...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if(checkFeilds())
                    {
                        auth.createUserWithEmailAndPassword(signup_email.getText().toString(),conf_password.getText().toString())
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Signup.this, "Signup failed. already registered?" ,//here add the prompt
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            User u = new User(signup_email.getText().toString(),conf_password.getText().toString());
                                            UserCreds userCreds = new UserCreds(context);
                                            userCreds.setIsUserSet(true);
                                            userCreds.setUser(u);
                                            mDatabaseReference.push().setValue(u);
                                            progressDialog.dismiss();
                                            Toast.makeText(context,"Welcome!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Signup.this, MainActivity.class));
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

        open_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }

    public boolean checkFeilds() {
        signup_email = (EditText) findViewById(R.id.signup_email);
        signup_password = (EditText) findViewById(R.id.signup_password);
        conf_password = (EditText) findViewById(R.id.conf_password);
        if(!signup_email.getText().toString().contains("@imedisecure.in"))
        {
            signup_email.setError("invalid email id!");
            return false;
        }
        if(signup_password.getText().toString().length()<6)
        {
            signup_password.setError("password should contain atleast 6 characters!");
            return  false;
        }
        if(signup_password.getText().toString().contains(" "))
        {
            signup_password.setError("cannot contain blank spaces");
            return false;
        }
        if(!signup_password.getText().toString().equals(conf_password.getText().toString()))
        {
            conf_password.setError("both the  passswords should match!");
            return  false;
        }
        return true;
    }
}
