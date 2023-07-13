package com.quizee.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quizee.Db;
import com.quizee.MainActivity;
import com.quizee.ProfSession;
import com.quizee.R;
import com.quizee.StudentSession;
import com.quizee.ui.login.LoginViewModel;
import com.quizee.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {
    private Db database;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);
        database=new Db(getApplicationContext());

        ((Button)findViewById(R.id.streg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflator=getLayoutInflater();
                View tmp=inflator.inflate(R.layout.student_register,null, false);
                tmp.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.slide_in_left));
                setContentView(tmp);

                ((Button)findViewById(R.id.stlog)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });




                ( (Button)(findViewById(R.id.register))).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name=((EditText)findViewById(R.id.rgstudentname)).getText().toString().trim();
                        String email=((EditText)findViewById(R.id.rgstudentmail)).getText().toString().trim();
                        String student_id=((EditText)findViewById(R.id.rgstudentid)).getText().toString().trim();
                        String password=((EditText)findViewById(R.id.rgpasswd)).getText().toString().trim();
                        database.jquery("insert into Students(student_name,email,student_id,password) values ('"+name+"','"+email+"','"+student_id+"','"+password+"')");
                        MainActivity.disp("Registration Successful Go to Login");
                        
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });









            }
        });

            
        ((Button)(findViewById(R.id.login))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=((EditText)findViewById(R.id.lgstudentid)).getText().toString().trim();
                String password=((EditText)findViewById(R.id.lgpasswd)).getText().toString().trim();
                Cursor r=database.dquery("select num,student_id,password from Students");
                int student=0;
                while(r.moveToNext()) {
                    if (r.getString(1).equals(email) && r.getString(2).equals(password)) {
                        student = r.getInt(0);
                        break;
                    }
                }
                if(student==0)
                    MainActivity.disp("Incorrect Login");
                else
                {
                    Intent myIntent = new Intent(LoginActivity.this, StudentSession.class);
                    myIntent.putExtra("Student", student); //Optional parameters
                    LoginActivity.this.startActivity(myIntent);
                }

            }});



        






    }


}
