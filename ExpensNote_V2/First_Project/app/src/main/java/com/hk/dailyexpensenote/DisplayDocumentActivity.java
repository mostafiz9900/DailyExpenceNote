package com.hk.dailyexpensenote;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayDocumentActivity extends AppCompatActivity {
private TextView titleFoundMessage;
private ImageView expenseDocumentimage;
private DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_document);
        this.setTitle("Document");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        titleFoundMessage=findViewById(R.id.documentFoundTitleTV);
        expenseDocumentimage=findViewById(R.id.expenseMemo);
        helper=new DatabaseHelper(this);




        //showing document operation
        String getId=getIntent().getStringExtra("Id");
        int id=Integer.valueOf(getId);


        Cursor cursor=helper.getDocument(id);
        while (cursor.moveToNext()){
            String documentUrl=cursor.getString(cursor.getColumnIndex(helper.COL_Document));
            if (documentUrl.equals("null")||documentUrl.equals("")){
                expenseDocumentimage.setVisibility(View.GONE);
                titleFoundMessage.setText("Not found any document");
            }
            else{
                Bitmap bitmap=decodeBase64(documentUrl);
                expenseDocumentimage.setVisibility(View.VISIBLE);
                expenseDocumentimage.setImageBitmap(bitmap);
            }



        }


    }


    //decode String to Bitmap

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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
