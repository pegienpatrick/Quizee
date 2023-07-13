package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.quizee.ui.login.LoginActivity;

public class professor_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_login);

        ((Button)findViewById(R.id.prreg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflator=getLayoutInflater();
                View tmp=inflator.inflate(R.layout.professor_register,null, false);
                tmp.startAnimation(AnimationUtils.loadAnimation(professor_login.this, android.R.anim.slide_in_left));
                setContentView(tmp);

                ((Button)findViewById(R.id.prlog)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);


                    }
                });

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(professor_login.this, android.R.layout.simple_spinner_item, new String[]{"Student","Professor"});

                ((Spinner)findViewById(R.id.role)).setAdapter(dataAdapter);
                ( (Button)(findViewById(R.id.rgprof))).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name=((EditText)findViewById(R.id.rgname)).getText().toString().trim();
                        String email=((EditText)findViewById(R.id.rgprofemail)).getText().toString().trim();
                        String role=((Spinner)findViewById(R.id.role)).getSelectedItem().toString().trim();
                        String password=((EditText)findViewById(R.id.rgpasswd)).getText().toString().trim();
                        Cursor tmp=MainActivity.database.dquery("select * from user where email_id='"+email+"'");
                        if(tmp.getCount()==0) {
                            MainActivity.database.jquery("insert into user(name,email_id,role,password) values ('" + name + "','" + email + "','" + role + "','" + password + "')");
                            MainActivity.disp("Registration Successful Go to Login");
                        }
                        else
                        {
                            MainActivity.disp("Email already registered");
                        }
                        
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });





                

                


                
            }
        });


        ((Button)(findViewById(R.id.forgotpassword))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(professor_login.this,ResetPassword.class);
                professor_login.this.startActivity(i);
            }
            });




        ((Button)(findViewById(R.id.login))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Db database=new Db(getApplicationContext());
                String email=((EditText)findViewById(R.id.lgemail)).getText().toString().trim();
                String password=((EditText)findViewById(R.id.lgpasswd)).getText().toString().trim();
                Cursor r=database.dquery("select email_id,password,role from user");
                String myuser="";
                String role="";
                while(r.moveToNext()) {
                    if (r.getString(0).equals(email) && r.getString(1).equals(password)) {
                        myuser = r.getString(0);
                        role=r.getString(2);
                        break;
                    }
                }
                if(myuser.length()==0)
                    MainActivity.disp("Incorrect Login");
                else
                {
                    Intent myIntent=null;
                    if(role.equals("Professor"))
                        myIntent = new Intent(professor_login.this, ProfSession.class);
                    else
                        myIntent = new Intent(professor_login.this, StudentSession.class);

                    myIntent.putExtra("user", myuser); //Optional parameters
                    professor_login.this.startActivity(myIntent);
                }

            }});
    }
}
