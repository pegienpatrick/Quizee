package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Build.VERSION_CODES.S;

public class MyExams extends AppCompatActivity {
    private String myuser;
    private Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exams);

        database=new Db(MyExams.this);
        myuser=getIntent().getExtras().getString("user");

        ((Button)findViewById(R.id.Cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyExams.this,StudentSession.class);
                intent.putExtra("user",myuser);
                MyExams.this.startActivity(intent);
            }
        });

        config();

    }



    public void config(){

        Cursor r= database.dquery("select title,course,start,end,duration,(select name from user where email_id=Quize.owner),Quize.num,(select sum(marks) from Question where quize=Quize.num and marks is not null),course from Quize join user on Quize.owner=user.email_id where end>='"+new Date().getTime()+"' and  course in (select course from CourseEnrollment where student='"+myuser+"') and Quize.num not in(select quize from exams_Results where marks is not null) order by start asc");
        while(r.moveToNext()){
            TextView pk=new TextView(MyExams.this);
            pk.setGravity(Gravity.CENTER);
            pk.setText(r.getString(1)+"   By  "+r.getString(5));
            ((LinearLayout)findViewById(R.id.allExams)).addView(pk);

            TextView ps=new TextView(MyExams.this);
            ps.setGravity(Gravity.CENTER);
            ps.setText(r.getString(0)+"   :  "+r.getString(4)+" Hrs");
            ((LinearLayout)findViewById(R.id.allExams)).addView(ps);

            SimpleDateFormat smt=new SimpleDateFormat("E dd-MMM-yyyy 'at' HH:mm:ss");
            TextView pt=new TextView(MyExams.this);
            pt.setGravity(Gravity.CENTER);
            pt.setText(smt.format(new Date(r.getLong(2)))+"   -  "+smt.format(new Date(r.getLong(3))));
            ((LinearLayout)findViewById(R.id.allExams)).addView(pt);

            TextView mks=new TextView(MyExams.this);
            mks.setText("( "+r.getString(7)+"   Marks )");
            mks.setGravity(Gravity.CENTER);
            ((LinearLayout)findViewById(R.id.allExams)).addView(mks);

            Button start=new Button(MyExams.this);
            start.setBackgroundColor(Color.BLUE);
            start.setTextColor(Color.WHITE);
            start.setText("START EXAM");

            final int num=r.getInt(6);
            String course=r.getString(8);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //MainActivity.disp(" i am "+num);

                    database.jquery("insert into exams_Results(quize,enroll,start) select '"+num+"',CourseEnrollment.num,'"+(new Date().getTime())+"' from CourseEnrollment where course='"+course+"' and student='"+myuser+"'");
                    Intent intent=new Intent(MyExams.this,DoExam.class);
                    intent.putExtra("user",myuser);
                    intent.putExtra("Quize",num);

                    MyExams.this.startActivity(intent);

                }
            });

            if(r.getLong(2)<=(new Date().getTime()))
                ((LinearLayout)findViewById(R.id.allExams)).addView(start);


            Space sp=new Space(MyExams.this);
            ((LinearLayout)findViewById(R.id.allExams)).addView(sp);
            sp.getLayoutParams().height=100;



        }


    }
}
