package com.hk.dailyexpensenote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

public class DetailExpenseSheet extends BottomSheetDialogFragment {
    private TextView date,amount,type,time;
    private String getDate,getType,getTime;
    private int getAmount;

    public DetailExpenseSheet() {

    }

    @SuppressLint("ValidFragment")
    public DetailExpenseSheet(String getDate, String getType, String getTime, int getAmount) {
        this.getDate = getDate;
        this.getType = getType;
        this.getTime = getTime;
        this.getAmount = getAmount;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.details_expense_sheet,container);
        date=view.findViewById(R.id.dateTV);
        amount=view.findViewById(R.id.amountTV);
        type=view.findViewById(R.id.typeTV);
        time=view.findViewById(R.id.timeTV);


        if(time.equals("")){
            time.setText("No time Added");
        }
        else{
            time.setText(getTime);
        }

        date.setText(getDate);
        type.setText(getType);
        amount.setText(String.valueOf(getAmount));





        return view;
    }
}
