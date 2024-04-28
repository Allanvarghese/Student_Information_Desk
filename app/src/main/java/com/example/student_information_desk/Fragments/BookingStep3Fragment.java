package com.example.student_information_desk.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_information_desk.Adapter.MyTimeSlotAdapter;
import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Common.SpacesItemDecoration;
import com.example.student_information_desk.Interface.ITimeSlotLoadListener;
import com.example.student_information_desk.Model.TimeSlot;
import com.example.student_information_desk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;


public class BookingStep3Fragment extends Fragment implements  ITimeSlotLoadListener {

    //variable
    DocumentReference employeeDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    LocalBroadcastManager localBroadcastManager;
     Calendar selected_date;
    RecyclerView recyclerView_time_slot;
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;
    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance() {
        if (instance == null)
            instance= new BookingStep3Fragment();
        return instance;
    }

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date=Calendar.getInstance();
            date.add(Calendar.DATE,0);//ADD Current date
            loadAvailableTimeSlotOfEmployee(Common.CurrentEmployee.getEmployeeId(),
                    simpleDateFormat.format(date.getTime()));

        }
    };

    private void loadAvailableTimeSlotOfEmployee(String employeeId,final String bookDate) {

       // /Service_type/Finance/Counter/feX0BKcUX2OMAHSaQv1X/Sid_Help_line/7GKcwKNbaulhMgUL03sk
        employeeDoc= FirebaseFirestore.getInstance()
                .collection("Service_type")
                .document(Common.location)
                .collection("Counter")
                .document(Common.CurrentCounter.getCounterId())
                .collection("Sid_Help_line")
                .document(Common.CurrentEmployee.getEmployeeId());

            //get information of this employee
         employeeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()) //if employee available
                    {
                        //get information of booking
                        //if not created return empty
                        CollectionReference date= FirebaseFirestore.getInstance()
                                .collection("Service_type")
                                .document(Common.location)
                                .collection("Counter")
                                .document(Common.CurrentCounter.getCounterId())
                                .collection("Sid_Help_line")
                                .document(Common.CurrentEmployee.getEmployeeId())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot=task.getResult();
                                    if(querySnapshot.isEmpty())//if dont have any appointment
                                    {
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }
                                    else
                                    {
                                        //if have appointment
                                        List<TimeSlot> timeSlots=new ArrayList<>();
                                        for(QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                        iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
         });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iTimeSlotLoadListener=this;
        localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
        simpleDateFormat= new SimpleDateFormat("dd_MM__yyyy");//31-03-2024 this id key
        selected_date=Calendar.getInstance();
        selected_date.add(Calendar.DATE,0);//Init current date
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView=inflater.inflate(R.layout.fragment_booking_step_three,container,false);
        // Find RecyclerView by ID
        recyclerView_time_slot = itemView.findViewById(R.id.recycler_timeslot);
        calendarView=itemView.findViewById(R.id.calendarView);
        init(itemView);
        return itemView;
    }

    private void init(View itemView) {
        recyclerView_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
        recyclerView_time_slot.setLayoutManager(gridLayoutManager);
        recyclerView_time_slot.addItemDecoration(new SpacesItemDecoration(8));
        //calendar
        Calendar startDate=Calendar.getInstance();
        startDate.add(Calendar.DATE,0);

        Calendar endDate=Calendar.getInstance();
        endDate.add(Calendar.DATE,2);//2 day left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();


//        HorizontalCalendar horizontalCalendar=new HorizontalCalendar.Builder(itemView,R.id.calendarView)
//                .range(startDate,endDate)
//                .datesNumberOnScreen(1)
//                .mode(HorizontalCalendar.Mode.DAYS)
//                .defaultSelectedDate(startDate)
//                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTime(date);

                if (selected_date.getTimeInMillis() != selectedCalendar.getTimeInMillis()) {
                    selected_date = selectedCalendar;
                    loadAvailableTimeSlotOfEmployee(Common.CurrentEmployee.getEmployeeId(),
                            simpleDateFormat.format(selected_date.getTime()));
                }
            }
        });


//        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener(){
//            @Override
//            public void onDateSelected(Date date, int position) {
//                if(selected_date.getTimeInMillis() != date.getTimeInMillis())
//                {
//                    selected_date = date;//This code will not load again  if you select new day same with date selected
//                    loadAvailableTimeSlotOfEmployee(Common.CurrentEmployee.getEmployeeId(),
//                            simpleDateFormat.format(date.getTime()));
//                }
//            }
//
//        });



    }


    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {

        MyTimeSlotAdapter adapter=new MyTimeSlotAdapter(getContext(),timeSlotList);
        recyclerView_time_slot.setAdapter(adapter);
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSlotLoadEmpty() {

        MyTimeSlotAdapter adapter=new MyTimeSlotAdapter(getContext());
        recyclerView_time_slot.setAdapter(adapter);
    }

}
