package com.example.student_information_desk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.student_information_desk.Adapter.MyViewPageAdapter;
import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Common.NonSwipeViewPager;
import com.example.student_information_desk.Model.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    LocalBroadcastManager localBroadcastManager;

    CollectionReference employeeRef;
    private StepView stepView;
    private NonSwipeViewPager viewPager;
    private Button btnPrevious;
    private Button btnNext;


    // Broadcast receiver
    private  BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step=intent.getIntExtra(Common.KEY_STEP,0);
            if(step ==1)
            // Update UI based on received data
            Common.CurrentCounter = intent.getParcelableExtra(Common.KEY_COUNTER_STORE);

            else if (step == 2)
                Common.CurrentEmployee=intent.getParcelableExtra(Common.KEY_EMPLOYEE_SELECTED);

            btnNext.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Initialize views
        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        btnPrevious.setOnClickListener(v -> previiousClick());
        btnNext.setOnClickListener(v -> nextClick());

        // Setup step view
        setupStepView();

        // Initially set button colors
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);//we have 4 fragment so we need keep state of this 4 screen page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //Show step
                stepView.go(i,true);
                if(i==0)

                    btnPrevious.setEnabled(false);
                else
                    btnPrevious.setEnabled(true);

                //set disable btn next here
                btnNext.setEnabled(false);

                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void previiousClick() {
        if (Common.step==3 ||Common.step>0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void nextClick() {
        if(Common.step<3 || Common.step==0)
        {
            Common.step++;//Increase
            if(Common.step==1)//After choose counter
            {
                if(Common.CurrentCounter != null)
               loadEmployeeByCounter(Common.CurrentCounter.getCounterId());
            }
            else if (Common.step==2)//pick time slot
            {
                if(Common.CurrentEmployee != null)
                    loadTimeSlotOfEmployee(Common.CurrentEmployee.getEmployeeId());
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void loadTimeSlotOfEmployee(String employeeId) {
        //send local broadcast to fragment step3
        Intent intent=new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadEmployeeByCounter(String counterId) {

        //Now select all employees of sid and finance
        if (!TextUtils.isEmpty(Common.location)) {
            employeeRef = FirebaseFirestore.getInstance().collection("Service_type")
                    .document(Common.location)
                    .collection("Counter")
                    .document(counterId)
                    .collection("Sid_Help_line");
            employeeRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Employee> employees=new ArrayList<>();
                            for (QueryDocumentSnapshot employeeSnapshot : task.getResult())
                            {
                                Employee employee=employeeSnapshot.toObject(Employee.class);
                                employee.setPassword("");//Remove pwd because in client app
                                employee.setEmployeeId(employeeSnapshot.getId());//Get id of employee

                                employees.add(employee);
                            }
                            //send broadcast to booking step 2 fragment to load recycler
                            Intent intent=new Intent(Common.KEY_EMPLOYEE_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_EMPLOYEE_LOAD_DONE,employees);
                            localBroadcastManager.sendBroadcast(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void setColorButton() {
        btnNext.setBackgroundResource(btnNext.isEnabled() ? R.color.black : R.color.grey);
        btnPrevious.setBackgroundResource(btnPrevious.isEnabled() ? R.color.black : R.color.grey);
    }

    private void setupStepView() {

        List<String> stepList = new ArrayList<>();
        stepList.add("Service Type");
        stepList.add("Counter Selection");
        stepList.add("Time Selection");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }

}
