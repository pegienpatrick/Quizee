package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.quizee.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    public static Activity act;
    public static Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=this;
        //setContentView(R.layout.professor_login);
        database=new Db(this);
        Intent intent=new Intent(MainActivity.this,professor_login.class);
        MainActivity.this.startActivity(intent);

    }


    public static void disp(String text)
    {
        final String str=text;
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(act, str, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
