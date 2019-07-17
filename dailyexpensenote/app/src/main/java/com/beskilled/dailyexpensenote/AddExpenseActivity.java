package com.beskilled.dailyexpensenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.beskilled.dailyexpensenote.databinding.ActivityAddExpenseBinding;

public class AddExpenseActivity extends AppCompatActivity {
private ActivityAddExpenseBinding addExpenseBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addExpenseBinding= DataBindingUtil.setContentView(this,R.layout.activity_add_expense);
addExpenseBinding.addBackBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        onBackPressed();
    }
});
    }


}
