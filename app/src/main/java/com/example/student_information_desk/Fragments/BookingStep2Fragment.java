package com.example.student_information_desk.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_information_desk.Adapter.MyEmployeeAdapter;
import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Common.SpacesItemDecoration;
import com.example.student_information_desk.Model.Employee;
import com.example.student_information_desk.R;

import java.util.ArrayList;

public class BookingStep2Fragment extends Fragment {
    LocalBroadcastManager localBroadcastManager;
    RecyclerView recyclerView_Employee;
    private BroadcastReceiver employeeDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Employee> employeeArrayList=intent.getParcelableArrayListExtra(Common.KEY_EMPLOYEE_LOAD_DONE);
            //create adapter late
            MyEmployeeAdapter adapter=new MyEmployeeAdapter(getContext(),employeeArrayList);
            recyclerView_Employee.setAdapter(adapter);
        }
    };
    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance= new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBroadcastManager=LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(employeeDoneReceiver,new IntentFilter(Common.KEY_EMPLOYEE_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(employeeDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View itemView=inflater.inflate(R.layout.fragment_booking_step_two,container,false);
        // Find RecyclerView by ID
        recyclerView_Employee = itemView.findViewById(R.id.recycler_employee);
        initView();
        return itemView;
    }

    private void initView() {
        recyclerView_Employee.setHasFixedSize(true);
        recyclerView_Employee.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView_Employee.addItemDecoration(new SpacesItemDecoration(4));
    }
}
