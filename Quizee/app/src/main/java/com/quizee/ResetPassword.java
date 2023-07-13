package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ((Button) findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Db database=new Db(getApplicationContext());
                String email=((EditText)findViewById(R.id.lgemail)).getText().toString().trim();
                String password=((EditText)findViewById(R.id.lgpasswd)).getText().toString().trim();
                database.jquery("update user set password='"+password+"' where email_id='"+email+"'");
                MainActivity.disp("Password Changed successfully");
                Intent intent = new Intent(ResetPassword.this,professor_login.class);
                startActivity(intent);
            }
        });
    }
}
