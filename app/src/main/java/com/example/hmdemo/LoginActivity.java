package com.example.hmdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hmdemo.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText Loginnumber,Loginpw;
    private Button LoginButton;
    private TextView Adminuser,Notadmin;
    private ProgressDialog loadingbar;

    private String parentuser = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Loginnumber = (EditText) findViewById(R.id.lgin_edit_number);
        Loginpw = (EditText) findViewById(R.id.lgin_edit_password);
        LoginButton = (Button) findViewById(R.id.lgin_btn);
        Adminuser = (TextView) findViewById(R.id.admin_user_link);
        Notadmin = (TextView) findViewById(R.id.not_admin_user_link);
        loadingbar =new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                userlogin();
            }

            private void userlogin()
            {
                String phone = Loginnumber.getText().toString();
                String pw = Loginpw.getText().toString();


                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(LoginActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(pw))
                {
                    Toast.makeText(LoginActivity.this, "Enter the password", Toast.LENGTH_SHORT).show();
                }

                else
                    {
                    loadingbar.setTitle("Logging in");
                    loadingbar.setMessage("Please wait while we log you in");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();

                    Accessgranted(phone,pw);
                    }
            }

            private void Accessgranted(final String phone, final String pw)
            {
               final DatabaseReference ref;
               ref = FirebaseDatabase.getInstance().getReference();

               ref.addListenerForSingleValueEvent(new ValueEventListener()
               {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot)
                   {
                     if(snapshot.child(parentuser).child(phone).exists())
                     {
                         Users userdata = snapshot.child(parentuser).child(phone).getValue(Users.class);

                         if(userdata.getNumber().equals(phone))
                     {
                         if(userdata.getPassword().equals(pw))
                         {
                             if(parentuser.equals("Admin"))
                             {
                                 Toast.makeText(LoginActivity.this, "Admin Log in successfull", Toast.LENGTH_SHORT).show();
                                 loadingbar.dismiss();

                                 Intent intent = new Intent(LoginActivity.this,AdministratorActivity.class);
                                 startActivity(intent);
                             }
                             else
                                 {
                                     Toast.makeText(LoginActivity.this, "Log in successfull", Toast.LENGTH_SHORT).show();
                                     loadingbar.dismiss();

                                     Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                                     startActivity(intent);
                             }
                         }

                         else
                             {
                                 Toast.makeText(LoginActivity.this, "password is incorrect", Toast.LENGTH_SHORT).show();
                                 loadingbar.dismiss();
                         }
                     }
                     }
                     else
                         {
                             Toast.makeText(LoginActivity.this, "No such number exists...please enter valid number", Toast.LENGTH_SHORT).show();
                             loadingbar.dismiss();
                     }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });

        Adminuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                Adminuser.setVisibility(View.INVISIBLE);
                Notadmin.setVisibility(View.VISIBLE);
                parentuser = "Admin";
            }
        });
        Notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                Adminuser.setVisibility(View.VISIBLE);
                Notadmin.setVisibility(View.INVISIBLE);
                parentuser = "Users";
            }
        });
    }
}