package com.hk.dailyexpensenote;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
    private Spinner eTypeSpinner;
    private TextView fromDateTv,toDateTv;
    private ImageView fromDateBtn,toDateBtn;
    private String typeOfExpense;
    private int count=0;




private FloatingActionButton addButton;
private RecyclerView expenseRB;
private List<Expense>expenseList;
private AdapterExpense adapterExpense;
private DatabaseHelper helper;
private Context context;


    private Calendar calendar;
    private int year,month,fromDay,toDay,hour,minute;
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

        try {
            initFiltardata(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        initR();
        try {
            getdata();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return  view;
    }

    private void initFiltardata(final View view) throws ParseException {
        //get data from spinner and calender

        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(context,android.R.layout.simple_list_item_activated_1,typeExpense);
        eTypeSpinner.setAdapter(arrayAdapter);

        typeOfExpense=typeExpense[0];

        eTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfExpense=typeExpense[position];
                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        getdata();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );





        //customize date range within calander


        //date picker part
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        int indexMonth=month+1;
        fromDay=1;
        toDay=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);


        //fromDate

        String nFromDate=year+"/"+indexMonth+"/"+fromDay;
        fromDateTv.setText(nFromDate);


        final DatePickerDialog.OnDateSetListener fromDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int fromDay) {
                calendar.set(year,month,fromDay);
                String userFromDate=dateFormat.format(calendar.getTime());
               // Toast.makeText(context,userFromDate+" is selected",Toast.LENGTH_LONG).show();
                fromDateTv.setText(userFromDate);

                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        getdata();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        };

        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Date as begining of the month
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),fromDateListener,year,month,fromDay);
                datePickerDialog.show();



            }
        });



        //toDate

        String nToDate=year+"/"+indexMonth+"/"+toDay;
        toDateTv.setText(nToDate);


        final DatePickerDialog.OnDateSetListener toDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int toDay) {
                calendar.set(year,month,toDay);
                String userFromDate=dateFormat.format(calendar.getTime());
                toDateTv.setText(userFromDate);

                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        getdata();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }
        };

        toDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Date as begining of the month

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),toDateListener,year,month,toDay);
                datePickerDialog.show();



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




        expenseList.clear();                    //clear previous data
        adapterExpense.notifyDataSetChanged();

        Cursor cursor=helper.showAllData();
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(helper.COL_ID));
            String timeTo=cursor.getString(cursor.getColumnIndex(helper.COL_TIME));
            int amount=cursor.getInt(cursor.getColumnIndex(helper.COL_Amount));
            String type=cursor.getString(cursor.getColumnIndex(helper.COL_TYPE));

            long dateFromDB=cursor.getLong(cursor.getColumnIndex(helper.COL_Date));
            String dateTo=dateFormat.format(dateFromDB);
            count++;

           // Toast.makeText(context,"Check date : "+dateFromDB,Toast.LENGTH_LONG).show();


            if(typeOfExpense.equals(type)&&dateFromDB>=fromDate&&dateFromDB<=toDate){
                expenseList.add(new Expense(amount,type,dateTo,timeTo,id,null));  //add image documents next
                adapterExpense.notifyDataSetChanged();
           }
           else{
              // expenseList.clear();               //when not found data according to filtering
               //adapterExpense.notifyDataSetChanged();
            }



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
        eTypeSpinner =view.findViewById(R.id.showActivityTypeSpinnerID);
        fromDateTv=view.findViewById(R.id.viewFromDateTV);
        toDateTv=view.findViewById(R.id.viewToDateTV);


    }

}
