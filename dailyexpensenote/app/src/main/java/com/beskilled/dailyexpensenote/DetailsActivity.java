package com.beskilled.dailyexpensenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beskilled.dailyexpensenote.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {
private ActivityDetailsBinding detailsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       detailsBinding= DataBindingUtil.setContentView(this,R.layout.activity_details);

       detailsBinding.backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
           onBackPressed();
           }
       });
    }
}
