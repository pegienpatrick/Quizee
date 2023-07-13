package com.quizee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChooser extends LinearLayout implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
   public EditText editText;
   public Date date;
   private Context ctx;
   private SimpleDateFormat mdate;
   private SimpleDateFormat smt;
   private SimpleDateFormat jtime;
   public Runnable todo=null;
   
   public DateChooser(Context ctx,Date date,String hr)
   {
   		super(ctx);
       mdate=new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
   		this.ctx=ctx;
   	    this.date=date;
        String str=mdate.format(date).split(" ")[0];
        str+=" "+hr;
        try{

            this.date=mdate.parse(str);
        }catch(Exception e)
        {
            System.out.println(e);

        }
   		editText=new EditText(ctx);
   		editText.setOnClickListener(this);

   		smt=new SimpleDateFormat("E dd-MMM-yyyy");
        jtime=new SimpleDateFormat("HH:mm:ss");
   		editText.setText(smt.format(date));
        addView(editText);
        editText.setFocusable (false);
        editText.setBackground(new myutils.rshape(Color.TRANSPARENT,true));
        editText.setTextColor(Color.BLUE);
        editText.setTextSize(15);
        editText.setPadding(40,10,40,10);


   }


   
   @Override
   public void onClick(View v) {
        //System.out.println(date.getYear()+" "+date.getMonth()+" "+ date.getDate());
       DatePickerDialog dialog = new DatePickerDialog(ctx,this, 1900+date.getYear(), date.getMonth(), date.getDate());
       dialog.show();
   }
   @Override
   public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
       System.out.println("yea"+mdate.format(date));
       //this.editText.setText();
    try{
        date=mdate.parse(dayOfMonth+"-"+(monthOfYear+1)+"-"+year+" "+jtime.format(date));
    }catch(Exception e)
    {
        System.out.println(e);
    }
        editText.setText(smt.format(date));
        new Thread(todo).start();
   }



}