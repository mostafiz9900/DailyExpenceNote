package com.hk.dailyexpensenote;


import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
private ImageView fromDatePickerBtn,toDatePickerbtn;
    private Spinner typeSpinner;
    private TextView fromTV,toTV,totalCostTv;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
    private String type;
    private Calendar calendar;
    private int year,month,fromDay,toDay,hour,minute;
    private Context context;
    private DatabaseHelper helper;
    private int totAmount=0;
    private int count=0;
    int currentPosition=0;
    private long  fdate,tdate;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=container.getContext();
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        typeSpinner=view.findViewById(R.id.dBoardtypeSpinnerID);
        fromDatePickerBtn=view.findViewById(R.id.dashBoardfromDateCalenderBtn);
        toDatePickerbtn=view.findViewById(R.id.dashBoardtoDateCalenderBtn);
        fromTV=view.findViewById(R.id.dashBoardFromDateTV);
        toTV=view.findViewById(R.id.dashBoardtoDateTV);
        totalCostTv=view.findViewById(R.id.totalCostTV);
        helper=new DatabaseHelper(context);


        try {
            process();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            pullData();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void process() throws ParseException {
        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_activated_1,typeExpense);
        typeSpinner.setAdapter(arrayAdapter);


        type=typeExpense[0];
        typeSpinner.setSelection(currentPosition);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPosition=position;
                type=typeExpense[position];
                typeSpinner.setSelection(position);
                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        pullData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );


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
        fromTV.setText(nFromDate);


        final DatePickerDialog.OnDateSetListener fromDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int fromDay) {
                calendar.set(year,month,fromDay);
                String userFromDate=dateFormat.format(calendar.getTime());
                //Toast.makeText(context,userFromDate+" is selected",Toast.LENGTH_LONG).show();
                fromTV.setText(userFromDate);
                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        pullData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        fromDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Date as begining of the month
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),fromDateListener,year,month,fromDay);
                datePickerDialog.show();



            }
        });



        //toDate

        String nToDate=year+"/"+indexMonth+"/"+toDay;
        toTV.setText(nToDate);


        final DatePickerDialog.OnDateSetListener toDateListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int toDay) {
                calendar.set(year,month,toDay);
                String userFromDate=dateFormat.format(calendar.getTime());
                toTV.setText(userFromDate);
                if(count>0){                     //set listener to pull modyifiying data when second time data set
                    try {
                        pullData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        toDatePickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Date as begining of the month

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),toDateListener,year,month,toDay);
                datePickerDialog.show();



            }
        });





        /*
        Cursor cursor=helper.showTotalExpense(fdate,tdate,type);

        while (cursor.moveToNext()){
            int totalExpense=cursor.getInt(cursor.getColumnIndex(helper.TOT_EXPENSE));   //getTotal amount
            totalCostTv.setText("BDT "+totalExpense);


        }

*/






    }

    private void pullData() throws ParseException {
        //getTotal expense by  type from DataBase and set Total Expense
        Date d1=dateFormat.parse(fromTV.getText().toString());
        Date d2=dateFormat.parse(toTV.getText().toString());
        fdate=d1.getTime();
        tdate=d2.getTime();



        int dbAmount=0;
        Cursor cursor=helper.showAllData();
        while (cursor.moveToNext()){
            long dateFromDB=cursor.getLong(cursor.getColumnIndex(helper.COL_Date));
            String dbtype=cursor.getString(cursor.getColumnIndex(helper.COL_TYPE));
            count++;
            if(type.equals(dbtype)&&dateFromDB>=fdate&&dateFromDB<=tdate){
                dbAmount=cursor.getInt(cursor.getColumnIndex(helper.COL_Amount));
                totAmount=totAmount+dbAmount;
            }

        }


        totalCostTv.setText(totAmount+"Tk");
        totAmount=0;
    }

}
