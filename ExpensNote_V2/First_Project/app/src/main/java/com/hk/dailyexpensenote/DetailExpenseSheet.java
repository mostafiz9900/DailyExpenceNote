package com.hk.dailyexpensenote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailExpenseSheet extends BottomSheetDialogFragment {
    private TextView date,amount,type,time;
    private Button showDocument;
    private String getDate,getType,getTime;
    private int id,getAmount;

    public DetailExpenseSheet() {

    }

    @SuppressLint("ValidFragment")
    public DetailExpenseSheet(int id,String getDate, String getType, String getTime, int getAmount) {
        this.getDate = getDate;
        this.getType = getType;
        this.getTime = getTime;
        this.id=id;
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
        showDocument=view.findViewById(R.id.showDocumentBtnID);


        if(getTime.equals("")){
            time.setText("No time Added");
        }
        else{
            time.setText(getTime);
        }

        date.setText(getDate);
        type.setText(getType);
        amount.setText(String.valueOf(getAmount));


//go to another activity to show documents
        showDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DisplayDocumentActivity.class);
                intent.putExtra("Id",String.valueOf(id));
                startActivity(intent);

            }
        });





        return view;
    }
}
