package com.hk.dailyexpensenote;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdatExpense extends AppCompatActivity {
    private Spinner typeSpinner;
    private EditText amountET,dateET,timeET;
    private Button updateDocumentBtn,updateExpenseBtn;
    private ImageView datePickBtn,timePickBtn;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
    private String typeOfExpense;

    private int selectposition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updat_expense);
        init();

    }

    private void init() {
        typeSpinner=findViewById(R.id.typeSpinnerID);
        amountET=findViewById(R.id.expenseAmountET);
        dateET=findViewById(R.id.expenseDateET);
        timeET=findViewById(R.id.expenseTimeET);
        updateDocumentBtn =findViewById(R.id.updateDocumentButton);
        updateExpenseBtn =findViewById(R.id.updateExpenseBtn);
        datePickBtn=findViewById(R.id.datePickerBtn);
        timePickBtn=findViewById(R.id.timePickerBtn);
        helper=new DatabaseHelper(UpdatExpense.this);

        //update value..
        final String rId=getIntent().getStringExtra("id");
        typeOfExpense=getIntent().getStringExtra("type");
        String rDate=getIntent().getStringExtra("date");
        String rTime=getIntent().getStringExtra("time");
        String rAmount=getIntent().getStringExtra("amount");

        dateET.setText(rDate);
        timeET.setText(rTime);
        amountET.setText(rAmount);


        //custom set
        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,typeExpense);
        typeSpinner.setAdapter(arrayAdapter);


        //find position thats selected by user
        for (int i=0;i<typeExpense.length;i++){
            if(typeOfExpense.equals(typeExpense[i])){
                selectposition=i;
            }
        }

        typeSpinner.setSelection(selectposition);                  //set spinner position thats request for update



        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfExpense=typeExpense[position];
                Toast.makeText(getApplicationContext(),typeOfExpense+" is selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );


        //get date and time into dateET and Time ET

        datePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=getLayoutInflater().inflate(R.layout.custom_date_picker,null);

                Button done=view.findViewById(R.id.doneButton);
                final DatePicker datePicker=view.findViewById(R.id.datePickerDialogue);
                builder.setView(view);
                final Dialog dialog=builder.create();
                dialog.show();
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day=datePicker.getDayOfMonth();
                        int month=datePicker.getMonth();
                        month=month+1;
                        int year=datePicker.getYear();

                        String cDate=year+"/"+month+"/"+day;
                        Date d=null;
                        try {
                            d=dateFormat.parse(cDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String fdate=dateFormat.format(d);
                        dateET.setText(fdate);
                        dialog.dismiss();

                    }
                });








            }
        });


        timePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=getLayoutInflater().inflate(R.layout.custom_time_picker,null);

                Button doneTime=view.findViewById(R.id.doneTimeButton);
                final TimePicker timePicker=view.findViewById(R.id.timePickerDialogue);
                builder.setView(view);
                final Dialog dialog=builder.create();
                dialog.show();
                doneTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        @SuppressLint({" NewApi", "NewApi", "LocalSuppress"}) int hour=timePicker.getHour();
                        @SuppressLint({"NewApi", "LocalSuppress"}) int min=timePicker.getMinute();

                        Time time=new Time(hour,min,0);

                        String ftime=timeFormat.format(time);
                        timeET.setText(ftime);
                        dialog.dismiss();

                    }
                });

            }
        });



        //call for update on database
//update data button
        updateExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userDate=dateET.getText().toString();
                Date d= null;
                try {
                    d = dateFormat.parse(userDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mdate=d.getTime();
                String uTime=timeET.getText().toString();
                int uAmount= Integer.parseInt(amountET.getText().toString());
                helper.update(rId,typeOfExpense,uAmount,mdate,uTime,"");
                Toast.makeText(UpdatExpense.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                finish();   //need to add fragment for real time data change view
            }
        });




    }
}
