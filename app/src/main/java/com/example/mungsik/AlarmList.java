package com.example.mungsik;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmList implements Serializable {
    private Date date;
    private int ID;

    public AlarmList(Date date, int id)
    {
        this.date = date;
        this.ID = id;
    }

    public void setDate(Date date) {
        this.date = date ;
    }

    public Date getDate() {
        return this.date ;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID(){
        return ID;
    }

    public String getIDStr()
    {
        return Integer.toString(ID);
    }

    final public String changeAMPM() {
        String date = new SimpleDateFormat("HH").format(getDate());
        String full_date = "";
        int hour = Integer.parseInt(date);
        if (hour > 12) {
            full_date += "오후 ";
        }
        else {
            full_date += "오전 ";
        }
        return full_date += new SimpleDateFormat("hh : mm").format(getDate());
    }
}