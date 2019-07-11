package com.hk.dailyexpensenote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
private Spinner typeSpinner;
private EditText amountET,dateET,timeET;
private Button addDocument,addExpense;
private ImageView datePickBtn,timePickBtn;
private String typeOfExpense;
private DatabaseHelper helper;


@SuppressLint("SimpleDateFormat")
private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
private SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        init();
        process();
    }

    private void process() {

        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,typeExpense);
        typeSpinner.setAdapter(arrayAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfExpense=typeExpense[position];
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









//add value into database
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeOfExpense.equals("")||amountET.toString().equals("")||dateET.toString().equals("")) {
                    if (amountET.getText().toString().equals("")) {
                        amountET.setError("please enter amount");
                        amountET.requestFocus();
                    } else if (dateET.getText().toString().equals("")) {
                        dateET.setError("please enter date from date picker");
                        dateET.requestFocus();
                    }

                    else if (typeOfExpense.equals("")) {
                        Toast.makeText(getApplicationContext(),"Enter Expense type please..",Toast.LENGTH_LONG).show();
                    }

                }

                else{
                    //add type,date,time,amount value into database
                    int uamount=Integer.valueOf(amountET.getText().toString());
                    String userDate=dateET.getText().toString();
                    Date d= null;
                    try {
                        d = dateFormat.parse(userDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long mdate=d.getTime();
                    String userTime=timeET.getText().toString();

                    long id=helper.insertData(typeOfExpense,uamount,mdate,userTime,"null");

                    if(id==-1){
                        Toast.makeText(AddExpenseActivity.this,"Error : Data  can not Inserted.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddExpenseActivity.this,"Expense row : "+id+" is Inserted.",Toast.LENGTH_SHORT).show();

                        //back to fragment
                        ExpenseFragment expenseFragment=new ExpenseFragment();


                        finish(); //need to add fragment for real time data change view




                    }





                }
            }
        });

















    }

    private void init() {
        typeSpinner=findViewById(R.id.typeSpinnerID);
        amountET=findViewById(R.id.expenseAmountET);
        dateET=findViewById(R.id.expenseDateET);
        timeET=findViewById(R.id.expenseTimeET);
        addDocument =findViewById(R.id.addDocumentButton);
        addExpense =findViewById(R.id.addExpenseBtn);
        datePickBtn=findViewById(R.id.datePickerBtn);
        timePickBtn=findViewById(R.id.timePickerBtn);
        helper=new DatabaseHelper(AddExpenseActivity.this);


    }
}
