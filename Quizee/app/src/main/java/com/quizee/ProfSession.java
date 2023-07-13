package com.quizee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.quizee.ui.login.LoginActivity;

import java.util.Date;

public class ProfSession extends AppCompatActivity {
    public static int prof=0;
    public static String myuser;
    private Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_session);
        database=new Db(getApplicationContext());
        config();
        ((Button)findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ((Button)findViewById(R.id.newQuize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.jquery("insert into Quize(questions,owner,start,end) values('"+0+"','"+myuser+"','"+new Date().getTime()+"','"+(new Date().getTime()+3600000*3)+"')");
                Intent myIntent = new Intent(ProfSession.this, viewQuize.class);
                myIntent.putExtra("quize", 0); //Optional parameters
                ProfSession.this.startActivity(myIntent);
            }
        });




    }




    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to log out ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                PackageManager packageManager = getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                startActivity(mainIntent);
                Runtime.getRuntime().exit(0);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

public void config(){
    myuser=getIntent().getExtras().getString("user");
        Cursor r=database.dquery("select name,email_id,course from user where email_id='"+myuser+"'");
    if(r.moveToFirst())
    {
        ((TextView)findViewById(R.id.whoami)).setText(r.getString(0)+" ("+r.getString(1)+") ");
        ((EditText)findViewById(R.id.course)).setText(r.getString(2));
    }
    ((EditText)findViewById(R.id.course)).addTextChangedListener(new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int aft )
        {

        }

        @Override
        public void afterTextChanged(Editable s) {
            database.jquery("update user set course='"+((EditText)findViewById(R.id.course)).getText()+"' where email_id='"+myuser+"'");

        }

    });

    Cursor res=database.dquery("select num,title from Quize where owner='"+myuser+"' order by num desc");
    final Button[] b=new Button[res.getCount()];
    final int[] index=new int[res.getCount()];
    int counter=0;
    while(res.moveToNext())
    {
        index[counter]=res.getInt(0);
        b[counter]=new Button(this);
        b[counter].setText(res.getString(1));
        b[counter].setBackgroundColor(Color.rgb(52,58,64));
        b[counter].setTextColor(Color.WHITE);
        Space s=new Space(this);
        ((LinearLayout)findViewById(R.id.holder)).addView((b[counter]));
        ((LinearLayout)findViewById(R.id.holder)).addView((s));
        s.getLayoutParams().height=70;
        final int ct=counter;
        b[counter].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfSession.this,viewQuize.class);
                intent.putExtra("quize",index[ct]);
                ProfSession.this.startActivity(intent);
            }
        });

    counter++;
    }
}

    @Override
    public void onBackPressed() {
        logout();
    }



}
