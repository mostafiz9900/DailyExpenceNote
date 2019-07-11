package com.hk.dailyexpensenote;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {
    private Spinner typeSpinner;
    private TextView fromDateTv,toDateTv;
    private ImageView fromDateBtn,toDateBtn;
    private String typeOfExpense;




private FloatingActionButton addButton;
private RecyclerView expenseRB;
private List<Expense>expenseList;
private AdapterExpense adapterExpense;
private DatabaseHelper helper;
private Context context;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        context=container.getContext();
        init(view);
        addData();
        initFiltardata(view);
        initR();
        try {
            getdata();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  view;
    }

    private void initFiltardata(View view) {
        //get data from spinner and calender

        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,typeExpense);
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


        //set from date and to date from date picker

      //from date when  user not selected
        final DatePicker datePicker=view.findViewById(R.id.datePickerDialogue);
        Calendar calendar=Calendar.getInstance();
        int dayOfmonth=1;
        int m= calendar.get(Calendar.MONTH);
        m=m+1;
        int y=calendar.get(Calendar.YEAR);
        String beginingMonthDate=y+"/"+m+"/"+dayOfmonth;
        Date d=null;
        try {
            d=dateFormat.parse(beginingMonthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sdate=dateFormat.format(d);
        fromDateTv.setText(sdate);

        //to date when  user not selected
        Date dTo=new Date();
        String tDate=dateFormat.format(dTo);
        toDateTv.setText(tDate);




        //after user selected
        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=getLayoutInflater().inflate(R.layout.custom_date_picker,null);

                Button done=view.findViewById(R.id.doneButton);

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
                        fromDateTv.setText(fdate);
                        dialog.dismiss();

                    }
                });








            }
        });



        //to date
        toDateBtn.setOnClickListener(new View.OnClickListener() {
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
                        toDateTv.setText(fdate);
                        dialog.dismiss();

                    }
                });








            }
        });




    }

    private void addData() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),AddExpenseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getdata() throws ParseException {
        //pull data from database and add to ArrayList using cursor
        Date d1=dateFormat.parse(fromDateTv.getText().toString());
        Date d2=dateFormat.parse(toDateTv.getText().toString());
        long fromDate=d1.getTime();
        long toDate=d2.getTime();


        Cursor cursor=helper.showAllData();
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(helper.COL_ID));
            String timeTo=cursor.getString(cursor.getColumnIndex(helper.COL_TIME));
            int amount=cursor.getInt(cursor.getColumnIndex(helper.COL_Amount));
            String type=cursor.getString(cursor.getColumnIndex(helper.COL_TYPE));

            long dateFromDB=cursor.getLong(cursor.getColumnIndex(helper.COL_Date));
            String dateTo=dateFormat.format(dateFromDB);
            expenseList.add(new Expense(amount,type,dateTo,timeTo,id,null));  //add image documents next
            adapterExpense.notifyDataSetChanged();

        }

    }

    private void initR() {
        expenseRB.setLayoutManager(new LinearLayoutManager(context));
        expenseRB.setAdapter(adapterExpense);
    }

    private void init(View view) {
        helper=new DatabaseHelper(context);
        addButton=view.findViewById(R.id.floatAB);
        expenseRB=view.findViewById(R.id.expenseRV);
        expenseList=new ArrayList<>();
        adapterExpense=new AdapterExpense(context,expenseList,helper);
        fromDateBtn=view.findViewById(R.id.fromDateCalenderBtn);
        toDateBtn=view.findViewById(R.id.toDateCalenderBtn);
        typeSpinner=view.findViewById(R.id.typeSpinnerID);
        fromDateTv=view.findViewById(R.id.fromDateTV);
        toDateTv=view.findViewById(R.id.toDateTV);


    }

}
