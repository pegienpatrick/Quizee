package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;

public class viewQuize extends AppCompatActivity {
private int quize;
private Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quize);
        quize=getIntent().getExtras().getInt("quize");
        database=new Db(getApplicationContext());
        config();
    }


    public void config(){
        Cursor r;
        if(quize==0)
            r=database.dquery("select num,title,duration,questions,owner,start,end from Quize where title is null or title = '' and owner='"+ProfSession.myuser+"'");
        else
            r=database.dquery("select num,title,duration,questions,owner,start,end from Quize where num='"+quize+"'");

        r.moveToFirst();
        final int qnum=r.getInt(0);
        final DateChooser opening=new DateChooser(this,new Date(r.getLong(5)),"");
        final DateChooser closing=new DateChooser(this,new Date(r.getLong(6)),"");
        ((TableRow)findViewById(R.id.opening)).addView(opening);
        ((TableRow)findViewById(R.id.closing)).addView(closing);

        ((EditText)findViewById(R.id.title)).setText(r.getString(1));
        ((EditText)findViewById(R.id.duration)).setText(r.getString(2));
        ((EditText)findViewById(R.id.questions)).setText(r.getString(3));
        //if(r.getInt(3)>0)
          //  ((EditText)findViewById(R.id.questions)).setEnabled(false);
        ((TimePicker)findViewById(R.id.timestart)).setIs24HourView(true);
        ((TimePicker)findViewById(R.id.timestart)).setCurrentHour(opening.date.getHours());
        ((TimePicker)findViewById(R.id.timestart)).setCurrentMinute(opening.date.getMinutes());
        ((TimePicker)findViewById(R.id.timestart)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                opening.date.setHours(timePicker.getHour());
                opening.date.setMinutes(timePicker.getMinute());
            }
        });

        ((TimePicker)findViewById(R.id.timeend)).setIs24HourView(true);
        ((TimePicker)findViewById(R.id.timeend)).setCurrentHour(closing.date.getHours());
        ((TimePicker)findViewById(R.id.timeend)).setCurrentMinute(closing.date.getMinutes());
        ((TimePicker)findViewById(R.id.timeend)).setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                closing.date.setHours(timePicker.getHour());
                closing.date.setMinutes(timePicker.getMinute());
            }
        });


        ((Button)findViewById(R.id.SAVE)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.jquery("update Quize set title='"+((EditText)findViewById(R.id.title)).getText()+"',duration='"+((EditText)findViewById(R.id.duration)).getText()+"',questions='"+((EditText)findViewById(R.id.questions)).getText()+"',start='"+opening.date.getTime()+"',end='"+closing.date.getTime()+"' where num='"+r.getString(0)+"'");
                Intent myIntent = new Intent(viewQuize.this,ProfSession.class);
                myIntent.putExtra("user", ProfSession.myuser); //Optional parameters
                viewQuize.this.startActivity(myIntent);
            }
        });

        ((Button)findViewById(R.id.question)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.jquery("update Quize set title='"+((EditText)findViewById(R.id.title)).getText()+"',duration='"+((EditText)findViewById(R.id.duration)).getText()+"',questions='"+((EditText)findViewById(R.id.questions)).getText()+"',start='"+opening.date.getTime()+"',end='"+closing.date.getTime()+"' where num='"+r.getString(0)+"'");

                Intent myIntent = new Intent(viewQuize.this,MyQuestions.class);
                myIntent.putExtra("quize", qnum); //Optional parameters
                viewQuize.this.startActivity(myIntent);
            }
        });

        ((Button)findViewById(R.id.results)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout myresults=new LinearLayout(viewQuize.this);
                myresults.setOrientation(LinearLayout.VERTICAL);
                myresults.setGravity(Gravity.CENTER);
                setContentView(myresults);

                TableLayout table=new TableLayout(viewQuize.this);
                myresults.addView(table);

                TableRow header=new TableRow(viewQuize.this);
                table.addView(header);

                TextView nm=new TextView(viewQuize.this);
                nm.setText("Student  ");
                header.addView(nm);
                header.setGravity(Gravity.CENTER);
                TextView mk=new TextView(viewQuize.this);
                mk.setText("    Marks");
                header.addView(mk);
                int n=1;
                Cursor r=database.dquery("select distinct email_id,name,marks from user join CourseEnrollment on user.email_id=CourseEnrollment.student join exams_Results on exams_Results.enroll=CourseEnrollment.num where quize='"+quize+"'");
                while(r.moveToNext()){
                    TableRow tb=new TableRow(viewQuize.this);
                    TextView t1=new TextView(viewQuize.this);
                    t1.setText(n+"."+  r.getString(0)+" "+r.getString(1));
                    tb.setGravity(Gravity.CENTER);
                    TextView t2=new TextView(viewQuize.this);
                    t2.setText("     "+r.getString(2));
                    tb.addView(t1);
                    tb.addView(t2);

                    n++;
                    table.addView(tb);
                }




            }
            });


        ((Button)findViewById(R.id.deleteQuize)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.jquery("delete from exams_Results where quize='"+quize+"'");
                database.jquery("delete from Question where quize='"+quize+"'");
                database.jquery("delete from choices where quize='"+quize+"'");
                database.jquery("delete from Quize where num='"+quize+"'");
                MainActivity.disp("Quize Deleted Successfully");

                Intent myIntent = new Intent(viewQuize.this,ProfSession.class);
                myIntent.putExtra("user", ProfSession.myuser); //Optional parameters
                viewQuize.this.startActivity(myIntent);



            }
            });




    }
}
