package com.quizee;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import java.util.Date;

public class MyNotifications extends Service {
    private Db database;
    private String myuser;
    public MyNotifications() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        myuser=intent.getExtras().getString("user");
        //MainActivity.disp("started :"+student);

        database=new Db(getApplicationContext());
        sendNotifications();

    }

    public void sendNotifications(){

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
            //MainActivity.disp(openExams.getString(0));
        }

      Cursor soonExams= database.dquery("select title,course,start,end,duration,(select name from user where email_id=Quize.owner),Quize.num,(select sum(marks) from Question where quize=Quize.num and marks is not null),course from Quize join user on Quize.owner=user.email_id where (start-43200000)<='"+new Date().getTime()+"' and end>='"+new Date().getTime()+"' and  course in (select course from CourseEnrollment where student='"+myuser+"') and Quize.num not in(select quize from exams_Results where marks is not null) order by start asc");


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
            //MainActivity.disp(Exams.getString(0));

        }


    }


    private int getNotificationIcon() {
        return R.drawable.ic_launcher_foreground;
    }
}
