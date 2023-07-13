package com.quizee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class DoExam extends AppCompatActivity {
    private String myuser;
    private int quize;
    private int enroll;
    private Db database;
    private static Thread counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exam);
        database=new Db(this);


        myuser=getIntent().getExtras().getString("user");
        quize=getIntent().getExtras().getInt("Quize");

        Cursor r=database.dquery("select duration,exams_Results.start,enroll,end from  exams_Results join Quize on exams_Results.quize=Quize.num where quize='"+quize+"'");

        r.moveToFirst();

        long duration=(long) r.getDouble(0)*3600000;
        long start=r.getLong(1);
        final long expectedEnd=start+duration;
        enroll=r.getInt(2);

        final long toend=new Date(r.getLong(3)).getTime();


        config();


        {
            counter = new Thread() {
                public void run() {
                    long mydate = new Date().getTime();
                    while (mydate < expectedEnd&&mydate<toend) {
                        long rem = expectedEnd - mydate;
                        int hrs = (int) (rem / 3600000);
                        int min = (int) ((rem % 3600000) / 60000);
                        int sec = (int) ((rem % 60000) / 1000);

                        try {
                            ((TextView) findViewById(R.id.timer)).setText(hrs + "Hr    " + min + " Min    " + sec + "  s");
                            Thread.sleep(990);
                        } catch (Exception e) {
                        }
                        mydate = new Date().getTime();
                    }
                    if(expectedEnd>1){
                        ((Button)findViewById(R.id.submit)).performClick();
                    }


                }

            };

            counter.start();
        }
    }


    public void config(){

        int num=1;
        Cursor r=database.dquery("select num,ask,answer,marks from Question where quize='"+quize+"' order by num desc");
        String[] answers=new String[r.getCount()];
        Double[] marks=new Double[r.getCount()];
        RadioGroup[] given=new RadioGroup[r.getCount()];
        String[][] choices=new String[r.getCount()][7];
        while(r.moveToNext())
        {
            answers[num-1]=r.getString(2);
            marks[num-1]=r.getDouble(3);
            TextView que=new TextView(DoExam.this);
            que.setText(num+". "+r.getString(1)+"   ("+r.getString(3)+" Marks)");
            ((LinearLayout)findViewById(R.id.holder)).addView(que);

            Cursor ch=database.dquery("select choice from Choices where question='"+r.getString(0)+"' order by num asc");
            given[num-1]=new RadioGroup(DoExam.this);
            ((LinearLayout)findViewById(R.id.holder)).addView(given[num-1]);
            int minindex=0;
            while(ch.moveToNext())
            {
                RadioButton rb=new RadioButton(DoExam.this);
                rb.setText(ch.getString(0));

                rb.setId(minindex);
                choices[num-1][minindex]=ch.getString(0);
                given[num-1].addView(rb);
                minindex++;
            }

            Space sp=new Space(DoExam.this);
            ((LinearLayout)findViewById(R.id.holder)).addView(sp);
            sp.getLayoutParams().height=100;


            num++;
        }

        ((Button)findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Double score=0.0;
                for(int i=0;i<answers.length;i++){
                    if(given[i].getCheckedRadioButtonId()!=-1)
                        if(choices[i][given[i].getCheckedRadioButtonId()].equals(answers[i]))
                            score+=marks[i];

                }
                database.jquery("update exams_Results set marks='"+score+"' where quize='"+quize+"' and enroll='"+enroll+"'");
                Toast.makeText(DoExam.this, " You Scored "+score, Toast.LENGTH_LONG).show();
                try{
                    Thread.sleep(100);
                }catch (Exception e){}

                Intent intent=new Intent(DoExam.this,StudentSession.class);
                intent.putExtra("user",myuser);
                DoExam.this.startActivity(intent);

            }
        });


    }



}
