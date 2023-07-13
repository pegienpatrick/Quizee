package com.quizee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;

import java.util.Date;

public class StudentSession extends AppCompatActivity {
    private static final String CHANNEL_ID ="Quizee" ;
    private String myuser;
    private Db database;
    private long lastNotifications=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_session);

        database=new Db(getApplicationContext());
        config();
        ((Button)findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ((Button) findViewById(R.id.myresults)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentSession.this,ExamResults.class);
                intent.putExtra("user",myuser);
                StudentSession.this.startActivity(intent);

            }
        });

        ((Button) findViewById(R.id.myexams)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(StudentSession.this,MyExams.class);
                        intent.putExtra("user",myuser);
                        StudentSession.this.startActivity(intent);
                    }
                });


        ((Button)findViewById(R.id.newCourse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.popup_window);
                // create the popup window

                ((Button) findViewById(R.id.Cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=getIntent();
                        StudentSession.this.startActivity(intent);
                    }
                });





                // dismiss the popup window when touched
                Cursor res=database.dquery("select course from user where course not in(select course from CourseEnrollment  where student='"+myuser+"') and role='Professor' ");
                final Button[] b=new Button[res.getCount()];

                int counter=0;
                while(res.moveToNext())
                {

                    b[counter]=new Button(StudentSession.this);
                    b[counter].setText(res.getString(0));
                    b[counter].setBackgroundColor(Color.rgb(52,58,64));
                    b[counter].setTextColor(Color.WHITE);
                    Space s=new Space(StudentSession.this);
                    ((LinearLayout)findViewById(R.id.holdi)).addView((b[counter]));
                    ((LinearLayout)findViewById(R.id.holdi)).addView((s));
                    s.getLayoutParams().height=70;
                    final int ct=counter;
                    b[counter].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            database.jquery("insert into CourseEnrollment(student,course) values ('"+myuser+"','"+b[ct].getText()+"')");
                            //popupWindow.dismiss();
                            //setContentView(R.lay);
                            Intent intent=getIntent();
                            StudentSession.this.startActivity(intent);
                        }
                    });

                    counter++;
                }



            }
            });

            sendNotifications();




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
        Cursor r=database.dquery("select name,email_id from user where email_id='"+myuser+"'");
        if(r.moveToFirst())
        {
            ((TextView)findViewById(R.id.whoami)).setText(r.getString(0)+" ("+r.getString(1)+") ");
        }

        Cursor res=database.dquery("select course from user where course in(select course from CourseEnrollment  where student='"+myuser+"')");
        final Button[] b=new Button[res.getCount()];

        int counter=0;
        while(res.moveToNext())
        {

            b[counter]=new Button(this);
            b[counter].setText(res.getString(0));
            b[counter].setBackgroundColor(Color.rgb(52,58,64));
            b[counter].setTextColor(Color.WHITE);
            Space s=new Space(this);
            ((LinearLayout)findViewById(R.id.holder)).addView((b[counter]));
            ((LinearLayout)findViewById(R.id.holder)).addView((s));
            s.getLayoutParams().height=70;


            counter++;
        }


        /*Intent intent=new Intent(StudentSession.this,MyNotifications.class);
        intent.putExtra("user",myuser);
        StudentSession.this.startService(intent);*/
        sendNotifications();

    }

    @Override
    public void onBackPressed() {
        logout();
    }



    public void sendNotifications(){
        //do_send();
        createNotificationChannel();
        Cursor openExams= database.dquery("select title,course,start,end,duration,(select name from user where email_id=Quize.owner),Quize.num,(select sum(marks) from Question where quize=Quize.num and marks is not null),course from Quize join user on Quize.owner=user.email_id where start<='"+new Date().getTime()+"' and end>='"+new Date().getTime()+"' and  course in (select course from CourseEnrollment where student='"+myuser+"') and Quize.num not in(select quize from exams_Results where marks is not null) order by start asc");

        /*Cursor =database.dquery("select title,Professors.course,start from Quize join Professors on Quize.prof=Professors.num  where start<='"+new Date().getTime()+"' and end>='"+new Date().getTime()+"' and Professors.num in (select course from CourseEnrollment where student='"+student+"') and Quize.num not in(select quize from exams_Results)");*/
        while(openExams.moveToNext())
        {
            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notif.cancelAll();
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle("Quizee Exam Open").setContentText(openExams.getString(0)+" ( "+openExams.getString(1)+" ) Exam is currently open. Go to the App and Finish it").
                    setContentTitle("Do your Quizee Exam").setSmallIcon(getNotificationIcon()).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
            MainActivity.disp(openExams.getString(0)+" ( "+openExams.getString(1)+" ) Exam is currently open. Go to the App and Finish it");
        }

      Cursor soonExams= database.dquery("select title,course,start,end,duration,(select name from user where email_id=Quize.owner),Quize.num,(select sum(marks) from Question where quize=Quize.num and marks is not null),course from Quize join user on Quize.owner=user.email_id where (start-43200000)<='"+new Date().getTime()+"' and end>='"+new Date().getTime()+"' and start>'"+new Date().getTime()+"' and  course in (select course from CourseEnrollment where student='"+myuser+"') and Quize.num not in(select quize from exams_Results where marks is not null) order by start asc");


       /* Cursor soonExams=database.dquery("select title,Professors.course,start from Quize join Professors on Quize.prof=Professors.num  where (start-43200000)<='"+new Date().getTime()+"' and end>='"+new Date().getTime()+"' and Professors.num in (select course from CourseEnrollment where student='"+student+"') ");*/
        while(soonExams.moveToNext())
        {
            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notif.cancelAll();
            Notification notify=new Notification.Builder
                    (getApplicationContext()).setContentTitle("Quizee Exam near").setContentText(soonExams.getString(0)+" ( "+soonExams.getString(1)+" ) Exam will soon Open. Go to the App and Check Details").
                    setContentTitle("Quizee Exam is almost").setSmallIcon(getNotificationIcon()).build();

            notify.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.notify(0, notify);
            MainActivity.disp(soonExams.getString(0)+" ( "+soonExams.getString(1)+" ) Exam will soon Open. Go to the App and Check Details");

        }


    }


    private int getNotificationIcon() {
        return R.drawable.ic_launcher_foreground;
    }


    private void do_send(){
        Context mContext=this;
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_001");
        Intent ii = new Intent(mContext.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Quizee");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(getNotificationIcon());
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                                                channelId,
                                                "Channel human readable title",
                                                NotificationManager.IMPORTANCE_HIGH);
           mNotificationManager.createNotificationChannel(channel);
          mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }


    private void createNotificationChannel() {
// Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
    NotificationCompat.Builder builder =  
                new NotificationCompat.Builder(this)  
                        .setSmallIcon(R.drawable.ic_launcher_foreground) //set icon for notification
                        .setContentTitle("Notifications Example") //set title of notification  
                        .setContentText("This is a notification message")//this is notification message  
                        .setAutoCancel(true) // makes auto cancel of notification  
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification  
  
  
        Intent notificationIntent = new Intent(this, NotificationView.class);  
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        //notification message will get at NotificationView  
        notificationIntent.putExtra("message", "This is a notification message");  
  
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  
                PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);  
  
        // Add as notification  
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
        manager.notify(0, builder.build());  
}






}
