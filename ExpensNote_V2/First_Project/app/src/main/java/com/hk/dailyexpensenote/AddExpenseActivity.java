package com.hk.dailyexpensenote;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
private ImageView documentImage;
private Bitmap bitmap=null;
private String documentURL="";

    private Calendar calendar;
private int hour,minute;



private LinearLayout cameraGalleryBtnField;
private String typeOfExpense;
private DatabaseHelper helper;


    public AddExpenseActivity() {
    }



    @SuppressLint("SimpleDateFormat")
private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
private SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_add_expense);
        this.setTitle("Add Expense"); getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(ActivityCompat.checkSelfPermission(AddExpenseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddExpenseActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return;
        }




        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);


        this.setTitle("Add Expense");

        init();
        process();
    }

    private void process() {

        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1,typeExpense);
        typeSpinner.setAdapter(arrayAdapter);

        typeOfExpense=typeExpense[0];

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





        //time picker listner
        final TimePickerDialog.OnTimeSetListener timePick=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Time time=new Time(hour,minute,0);
                calendar.setTime(time);
                String usertime = null;
                try {
                     usertime=timeFormat.format(calendar.getTime());
                }
                catch (Exception e){
                    Toast.makeText(AddExpenseActivity.this,"Please take the time first : "+e,Toast.LENGTH_SHORT).show();
                }

                timeET.setText(usertime);

            }
        };


        //time picker
        timePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddExpenseActivity.this,timePick,hour,minute,false);
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();





            }


        });





        //add Document  section
        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request for permission on amera or gallery
                cameraGalleryBtnField.setVisibility(View.VISIBLE);

            }
        });

















//add value into database
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amountET.getText().toString().equals("")||dateET.getText().toString().equals("")) {
                    if (amountET.getText().toString().equals("")) {
                        amountET.setError("please enter amount");
                        amountET.requestFocus();
                    } else if (dateET.getText().toString().equals("")) {
                        dateET.setError("please enter date from date picker");
                        dateET.requestFocus();
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
                    if(bitmap!=null){
                        documentURL=encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,100);
                    }






                    long id=helper.insertData(typeOfExpense,uamount,mdate,userTime,documentURL);

                    if(id==-1){
                        Toast.makeText(AddExpenseActivity.this,"Error : Data  can not Inserted.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddExpenseActivity.this,"Expense Data : "+id+" is Inserted.",Toast.LENGTH_SHORT).show();

                        //back to fragment

                       // ExpenseFragment expenseFragment=new ExpenseFragment();

                        finish(); //need to add fragment for real time data change view





                    }





                }
            }
        });





    }

    private void init() {
        typeSpinner=findViewById(R.id.addActivityTypeSpinnerID);
        amountET=findViewById(R.id.addActtivityexpenseAmountET);
        dateET=findViewById(R.id.addActivityexpenseDateET);
        timeET=findViewById(R.id.addActivityexpenseTimeET);
        addDocument =findViewById(R.id.addActivityaAddDocumentButton);
        addExpense =findViewById(R.id.addActivityAddExpenseBtn);
        datePickBtn=findViewById(R.id.addActivityDatePickerBtn);
        timePickBtn=findViewById(R.id.addActiviTimePickerBtn);
        documentImage=findViewById(R.id.fileIV);
        cameraGalleryBtnField=findViewById(R.id.cameraGalleryLLfield);

        helper=new DatabaseHelper(AddExpenseActivity.this);

    }







    //initialize camera ,gallery and cencle button listner

    public void cenclePickDocument(View view) {
        cameraGalleryBtnField.setVisibility(View.GONE);    //when press cencle button
    }


    public void openCamera(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }

    public void openGallery(View view) {
        Intent intent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            if(requestCode == 0){
                Bundle bundle = data.getExtras();
                 bitmap = (Bitmap) bundle.get("data");
                documentImage.setImageBitmap(bitmap);
                cameraGalleryBtnField.setVisibility(View.GONE);


            }
            else if(requestCode == 1){

                Uri uri = data.getData();
                 bitmap=null;
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                documentImage.setImageBitmap(bitmap);
                cameraGalleryBtnField.setVisibility(View.GONE);
                //set Image into ImageView


            }

        }
    }




    //encode  image to string url
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)    //example of parameter (mybitmap,Bitmap.CompressFormat.JPEG,100)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

//impliments back button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
