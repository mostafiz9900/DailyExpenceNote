package com.example.dailyexpensenote;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

private Spinner expenseTypeSpinner;
private List<String> expensesItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        // Spinner Drop down elements
        expensesItem = new ArrayList<String>();
        expensesItem.add("Food");
        expensesItem.add("Business Services");
        expensesItem.add("House Rent");
        expensesItem.add("Education Expenses");
        expensesItem.add("Car Rent");
        expensesItem.add("Travel Expense");

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,expensesItem);
        expenseTypeSpinner.setAdapter(dataAdapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void init() {
        expenseTypeSpinner=findViewById(R.id.expenceTypeSpinner);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_expences:
                    return true;
            }
            return false;
        }
    };

}
