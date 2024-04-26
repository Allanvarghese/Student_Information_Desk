package com.example.student_information_desk.Common;

import android.content.Intent;

import com.example.student_information_desk.Model.Counter;
import com.example.student_information_desk.Model.Employee;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT="ENABLE_BUTTON_NEXT";
    public static final String KEY_COUNTER_STORE = "COUNTER_SAVE";
    public static final String KEY_EMPLOYEE_LOAD_DONE = "EMPLOYEE_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP" ;
    public static final String KEY_EMPLOYEE_SELECTED = "EMPLOYEE_SELECTED";
    public static final int TIME_SLOT_TOTAL = 24;

    public static Counter CurrentCounter;
    public static int step = 0;//Init first step is 0
    public static String location="";
    public static Employee CurrentEmployee;

    public static String convertTimeSlotToString(int slot) {
        switch (slot)
        {
            case 0:
                return "9.00 - 9.15";
            case 1:
                return "9.15 - 9.30";
            case 2:
                return "9.30 - 9.45";
            case 3:
                return "9.45 - 10.00";
            case 4:
                return "10.00 - 10.15";
            case 5:
                return "10.15 - 10.30";
            case 6:
                return "10.30 - 10.45";
            case 7:
                return "10.45 - 11.00";
            case 8:
                return "11.00 - 11.15";
            case 9:
                return "11.15 - 11.30";
            case 10:
                return "11.30 - 11.45";
            case 11:
                return "11.45 - 12.00";
            case 12:
                return "12.00 - 12.15";
            case 13:
                return "12.15 - 12.30";
            case 14:
                return "12.30 - 12.45";
            case 15:
                return "12.45 - 13.00";
            case 16:
                return "13.00 - 13.15";
            case 17:
                return "13.15 - 13.30";
            case 18:
                return "13.30 - 13.45";
            case 19:
                return "13.45 - 14.00";
            case 20:
                return "14.00 - 14.15";
            case 21:
                return "14.15 - 14.30";
            case 22:
                return "14.30 - 14.45";
            case 23:
                return "14.45 - 15.00";
            default:
                return "Closed";



        }
    }
}
