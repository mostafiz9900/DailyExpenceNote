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
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

public class UpdatExpenseActivity extends AppCompatActivity {
    private Spinner typeSpinner;
    private EditText amountET,dateET,timeET;
    private Button updateExpenseBtn;
    private ImageView datePickBtn,timePickBtn;
    private ImageView filesetIV;
    private LinearLayout updateCameraGalleryBtnfield;
    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm aa");
    private String typeOfExpense;

    private String id;

    private Bitmap bitmap=null;
    private String documentURL="";

    private Calendar calendar;
    private int hour,minute;

    private int selectposition=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updat_expense);
        this.setTitle("Update Expense");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //request for permission on amera or gallery
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            return;
        }


        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);



        init();

    }

    private void init() {
        typeSpinner=findViewById(R.id.updateActivityTypeSpinnerID);
        amountET=findViewById(R.id.updateExpenseAmountET);
        dateET=findViewById(R.id.updateExpenseDateET);
        timeET=findViewById(R.id.updateExpenseTimeET);
        updateExpenseBtn =findViewById(R.id.updateExpenseBtn);
        datePickBtn=findViewById(R.id.updateActivityDatePickerBtn);
        timePickBtn=findViewById(R.id.updateActiviTimePickerBtn);
        helper=new DatabaseHelper(UpdatExpenseActivity.this);
        filesetIV=findViewById(R.id.updateFileIV);
        updateCameraGalleryBtnfield=findViewById(R.id.updateCameraGalleryBtnfield);

        //update value..
        final String rId=getIntent().getStringExtra("id");
        typeOfExpense=getIntent().getStringExtra("type");
        String rDate=getIntent().getStringExtra("date");
        String rTime=getIntent().getStringExtra("time");
        String rAmount=getIntent().getStringExtra("amount");
        id= getIntent().getStringExtra("id");       //get ID from request activity


        dateET.setText(rDate);
        timeET.setText(rTime);
        amountET.setText(rAmount);


        //custom set
        //set Type into spinner
        final String []typeExpense={"Rent","Food","Utility bills","Medicine","Cloathing","Transport","Health","Gift"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(UpdatExpenseActivity.this,android.R.layout.simple_list_item_activated_1,typeExpense);
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
                //Toast.makeText(getApplicationContext(),typeOfExpense+" is selected",Toast.LENGTH_SHORT).show();
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
                String usertime=timeFormat.format(calendar.getTime());
                timeET.setText(usertime);

            }
        };


        //time set

        timePickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    TimePickerDialog timePickerDialog=new TimePickerDialog(UpdatExpenseActivity.this,timePick,hour,minute,false);
                    timePickerDialog.show();







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
                helper.update(rId,typeOfExpense,uAmount,mdate,uTime);
                Toast.makeText(UpdatExpenseActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                finish();   //need to add fragment for real time data change view
            }
        });




    }


    public void updateOpenCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }

    public void updateOpenGallery(View view) {
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
                filesetIV.setImageBitmap(bitmap);
                changeDocumentData();                              //change database method called when update image
               updateCameraGalleryBtnfield.setVisibility(View.GONE);


            }
            else if(requestCode == 1){

                Uri uri = data.getData();
                bitmap=null;
                try {
                    bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filesetIV.setImageBitmap(bitmap);
                changeDocumentData();
                updateCameraGalleryBtnfield.setVisibility(View.GONE);
                //set Image into ImageView


            }

        }
    }

//cencle button
    public void UpdateCenclePickDocument(View view) {

        updateCameraGalleryBtnfield.setVisibility(View.GONE);    //when press cencle button

    }

    //add document button

    public void updateDocumentMethod(View view) {

        updateCameraGalleryBtnfield.setVisibility(View.VISIBLE);    //when press cencle button
    }

    public void changeDocumentData(){
        String uDocument = null;
        if(bitmap!=null){
            uDocument=encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,100);
        }

        if(!uDocument.equals("")){
            helper.updateDocument(id,uDocument);
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
