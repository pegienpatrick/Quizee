package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ExamResults extends AppCompatActivity {
    private Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_results);
        database=new Db(ExamResults.this);
        config();
    }


    public void config(){
        //int student=getIntent().getExtras().getInt("Student");
        String myuser=getIntent().getExtras().getString("user");
        ((Button)findViewById(R.id.Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ExamResults.this,StudentSession.class);
                intent.putExtra("user",myuser);
                ExamResults.this.startActivity(intent);
            }
        });

        final LinearLayout myresults=new LinearLayout(ExamResults.this);
        myresults.setOrientation(LinearLayout.VERTICAL);
        myresults.setGravity(Gravity.CENTER);
        //setContentView(myresults);

        TableLayout table=new TableLayout(ExamResults.this);
        myresults.addView(table);

        TableRow header=new TableRow(ExamResults.this);
        table.addView(header);

        TextView nm=new TextView(ExamResults.this);
        nm.setText("Exam  ");
        header.addView(nm);
        header.setGravity(Gravity.CENTER);
        TextView mk=new TextView(ExamResults.this);
        mk.setText("   Marks");
        header.addView(mk);
        int n=1;
        Cursor r=database.dquery("select distinct CourseEnrollment.course,title,marks from CourseEnrollment join exams_Results on exams_Results.enroll=CourseEnrollment.num join Quize on exams_Results.quize=Quize.num where student='"+myuser+"'");
        while(r.moveToNext()){
            TableRow tb=new TableRow(ExamResults.this);
            TextView t1=new TextView(ExamResults.this);
            t1.setText(n+"."+  r.getString(0)+" ("+r.getString(1)+")");
            tb.setGravity(Gravity.CENTER);
            TextView t2=new TextView(ExamResults.this);
            t2.setText("     "+r.getString(2));
            tb.addView(t1);
            tb.addView(t2);

            n++;
            table.addView(tb);
        }


        ((LinearLayout)findViewById(R.id.allResults)).addView(myresults);





    }
}
