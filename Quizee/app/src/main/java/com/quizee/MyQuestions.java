package com.quizee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import static com.quizee.MainActivity.database;
public class MyQuestions extends AppCompatActivity {
public Db database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_questions);

        database=new Db(getApplicationContext());

        config();





    }


    public void config(){
        int thequize=getIntent().getExtras().getInt("quize");
        Cursor r=database.dquery("select num,title,duration,questions,owner,start,end from Quize where num='"+thequize+"'");
        r.moveToFirst();

        EditText[] questions=new EditText[r.getInt(3)];
        Spinner[] answers=new Spinner[r.getInt(3)];
        EditText[] marks=new EditText[r.getInt(3)];
        EditText[][] choices=new EditText[r.getInt(3)][7];
        String[] correctAnswers=new String[r.getInt(3)];

        for(int i=0;i<r.getInt(3);i++){
            questions[i]=new EditText(this);
            //questions[i].setHint("Question");
            answers[i]=new Spinner(this);
            //answers[i].setSelection();
            marks[i]=new EditText(this);
            //marks[i].setHint("Marks");
            for(int j=0;j<7;j++)
                choices[i][j]=new EditText(this);
        }


        int counter=0;
        Cursor rq=database.dquery("select * from Question where quize='"+r.getInt(0)+"'");
        while(rq.moveToNext()&&counter<questions.length){
            correctAnswers[counter]=rq.getString(3);
            questions[counter].setText(rq.getString(1));
            marks[counter].setText(rq.getString(2));
            //answers[counter].setText(rq.getString(3));

            int kl=0;
            Cursor pc=database.dquery("select * from Choices where question='"+rq.getString(0)+"'");
            while(pc.moveToNext()){
                choices[counter][kl].setText(pc.getString(1));

                kl++;
            }

            counter++;
        }

        for(int i=0;i<r.getInt(3);i++){
            TextView tv=new TextView(this);
            tv.setText("Question "+(i+1));
            ((LinearLayout)findViewById(R.id.holder)).addView(tv);
            TextInputLayout qt=new TextInputLayout(this);
            qt.setHint("Question");
            ((LinearLayout)findViewById(R.id.holder)).addView(qt);
            qt.addView(questions[i]);
            int finalI = i;
            final String myanswer=correctAnswers[i];
            for(int j=0;j<7;j++){
                final String ms=myanswer;
                TextInputLayout qt2=new TextInputLayout(this);
                qt2.setHint("Choice");
                ((LinearLayout)findViewById(R.id.holder)).addView(qt2);
                qt2.setPadding(80,20,80,20);
                qt2.addView(choices[i][j]);
                choices[i][j].addTextChangedListener(new TextWatcher()
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
                    public void afterTextChanged(Editable s)
                    {

                        //call your function here of calculation here
                        List<String> list=new  ArrayList <String>();
                        for(int k=0;k<7;k++){
                            if(choices[finalI][k].getText().toString().trim().length()>0)
                                list.add(choices[finalI][k].getText().toString().trim());
                        }
                        answers[finalI].setAdapter(new ArrayAdapter<String>(MyQuestions.this, android.R.layout.simple_spinner_item, list));

                        for(int m=0;m<list.size();m++)
                            if(list.get(m).equals(myanswer))
                                answers[finalI].setSelection(m);

                    }
                });
            }

            TextView an=new TextView(this);
            an.setText("Choose Correct Answer Below");
            ((LinearLayout)findViewById(R.id.holder)).addView(an);

            TextInputLayout ans=new TextInputLayout(this);
            ans.setHint("Answer");
            ((LinearLayout)findViewById(R.id.holder)).addView(ans);
            ans.addView(answers[i]);

            TextInputLayout mar=new TextInputLayout(this);
            mar.setHint("Marks");
            ((LinearLayout)findViewById(R.id.holder)).addView(mar);
            mar.addView(marks[i]);
            marks[i].setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);




            answers[i].setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View view, MotionEvent motionEvent) {
                    List<String> list=new  ArrayList <String>();
                    for(int k=0;k<7;k++){
                        if(choices[finalI][k].getText().toString().trim().length()>0)
                            list.add(choices[finalI][k].getText().toString().trim());
                    }
                    answers[finalI].setAdapter(new ArrayAdapter<String>(MyQuestions.this, android.R.layout.simple_spinner_item, list));
                    for(int m=0;m<list.size();m++)
                        if(list.get(m).equals(myanswer))
                            answers[finalI].setSelection(m);
                    return false;
                }
            });

            List<String> list=new  ArrayList <String>();
            for(int k=0;k<7;k++){
                if(choices[finalI][k].getText().toString().trim().length()>0)
                    list.add(choices[finalI][k].getText().toString().trim());
            }
            answers[finalI].setAdapter(new ArrayAdapter<String>(MyQuestions.this, android.R.layout.simple_spinner_item, list));
            //answers[finalI].setSelection();
            for(int m=0;m<list.size();m++)
                if(list.get(m).equals(myanswer))
                    answers[finalI].setSelection(m);



            ((LinearLayout)findViewById(R.id.holder)).addView(createSpace(100,50));
        }

        ((Button)findViewById(R.id.SAVE)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //database.jquery("update Quize set title='"+((EditText)findViewById(R.id.title)).getText()+"',duration='"+((EditText)findViewById(R.id.duration)).getText()+"',questions='"+((EditText)findViewById(R.id.questions)).getText()+"',start='"+opening.date.getTime()+"',end='"+closing.date.getTime()+"' where num='"+r.getString(0)+"'");
                try {
                    database.jquery("delete from Question where quize='" + thequize + "'");
                }catch (Exception e){}
                try {
                    database.jquery("delete from Choices where quize='" + thequize + "'");
                }catch (Exception e){}

                for(int a=0;a<r.getInt(3);a++){
                    if(questions[a].getText().toString().length()>0){
                        database.jquery("insert into Question (quize,ask,marks,answer) values('"+thequize+"','"+questions[a].getText().toString().trim()+"','"+marks[a].getText().toString()+"','"+answers[a].getSelectedItem().toString()+"')");
                    Cursor pickit=database.dquery("select last_insert_rowid()");
                    pickit.moveToFirst();
                    for(int b=0;b<7;b++)
                        if(choices[a][b].getText().toString().length()>0){
                            database.jquery("insert into choices(choice,question,quize) values('"+choices[a][b].getText().toString().trim()+"','"+pickit.getString(0)+"','"+thequize+"')");
                        }

                    }
                }

                Intent myIntent = new Intent(MyQuestions.this,ProfSession.class);
                myIntent.putExtra("user", ProfSession.myuser); //Optional parameters
                MyQuestions.this.startActivity(myIntent);
            }
        });

    }



    public Space createSpace(int width,int height){
        Space tmp=new Space(this);
        tmp.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        return tmp;
    }










}
