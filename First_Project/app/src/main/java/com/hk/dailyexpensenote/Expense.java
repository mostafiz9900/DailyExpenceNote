package com.hk.dailyexpensenote;

import android.graphics.Bitmap;

import java.util.Date;

public class Expense {
    private int amount,id;
    private String type,date,time;
    private Bitmap imageDocument;

    public Expense(int amount, String type, String date, String time,int id,Bitmap imageDocument) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.time = time;
        this.id=id;
        this.imageDocument=imageDocument;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImageDocument() {
        return imageDocument;
    }
}
