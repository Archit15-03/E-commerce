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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.xml.datatype.Duration;

public class RegisterActivity extends AppCompatActivity {
    private EditText Inputname,Inputemail,Inputnumber,Inputpassword;
    private Button Registerbutton;
    private ProgressDialog loadbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Inputname = (EditText) findViewById(R.id.register_name);
        Inputemail = (EditText) findViewById(R.id.register_email);
        Inputnumber = (EditText) findViewById(R.id.register_number);
        Inputpassword = (EditText) findViewById(R.id.register_password);
        Registerbutton = (Button) findViewById(R.id.register_btn);
        loadbar = new ProgressDialog(this);

        Registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }

            private void CreateAccount() {
                String name = Inputname.getText().toString();
                String email = Inputemail.getText().toString();
                String number = Inputnumber.getText().toString();
                String password = Inputpassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(RegisterActivity.this, "Please enter name....", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Please enter email....", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(number)){
                    Toast.makeText(RegisterActivity.this, "Please enter number....", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Please enter password....", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadbar.setTitle("Create new account");
                    loadbar.setMessage("Wait while we check the information");
                    loadbar.setCanceledOnTouchOutside(false);
                    loadbar.show();

                    Validatenumber(name,number,password,email);

                }
            }

            private void Validatenumber(final String name, final String number, final String password, final String email) {
                final DatabaseReference ref;
                ref= FirebaseDatabase.getInstance().getReference();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!(snapshot.child("Users").child(number).exists()))
                        {
                            HashMap<String,Object> usermap = new HashMap<>();
                            usermap.put("name",name);
                            usermap.put("number",number);
                            usermap.put("password",password);
                            usermap.put("email",email);

                            ref.child("Users").child(number).updateChildren(usermap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                         if(task.isSuccessful())
                                         {
                                             Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                             loadbar.dismiss();

                                             Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                             startActivity(intent);
                                         }
                                         else
                                             {
                                                 loadbar.dismiss();
                                                 Toast.makeText(RegisterActivity.this, "Network issues......Check connction", Toast.LENGTH_SHORT).show();
                                         }
                                        }
                                    });

                        }
                        else
                            {
                                Toast.makeText(RegisterActivity.this, "This number" +number+"is already registered", Toast.LENGTH_SHORT).show();
                                loadbar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register with a new valid number", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
    
}