package com.example.student_information_desk.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_information_desk.Adapter.MyServiceTypeAdapter;
import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Common.SpacesItemDecoration;
import com.example.student_information_desk.Interface.IAllServiceTypeLoadListener;
import com.example.student_information_desk.Interface.ICounterLoadListener;
import com.example.student_information_desk.Model.Counter;
import com.example.student_information_desk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class BookingStep1Fragment extends Fragment implements IAllServiceTypeLoadListener, ICounterLoadListener {

    //variable

    CollectionReference service_typeRef;
    CollectionReference counterRef;

    MaterialSpinner spinner;
    RecyclerView recyclerCounter;

    IAllServiceTypeLoadListener iAllServiceTypeLoadListener;
    ICounterLoadListener iCounterLoadListener;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance() {
        if (instance == null)
            instance= new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service_typeRef = FirebaseFirestore.getInstance().collection("Service_type");
        iAllServiceTypeLoadListener = this;
        iCounterLoadListener=this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view= inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        // Find views by ID
        spinner = view.findViewById(R.id.spinner);
        recyclerCounter = view.findViewById(R.id.recycler_sid);
        initView();
        loadAllCounter();
        return view;
    }

    private void initView() {
        recyclerCounter.setHasFixedSize(true);
        recyclerCounter.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerCounter.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllCounter() {

        service_typeRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<String> list=new ArrayList<>();
                        list.add("Please choose your service type");
                        for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                            list.add(documentSnapshot.getId());
                        iAllServiceTypeLoadListener.ServiceTypeLoadSuccess(list);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iAllServiceTypeLoadListener.ServiceTypeLoadFaild(e.getMessage());
                    }
                });

    }

    @Override
    public void ServiceTypeLoadSuccess(List<String> serviceTypeList) {
        spinner.setItems(serviceTypeList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0) {

                    loadCounterOfSid(item.toString());
                }
                else
                    recyclerCounter.setVisibility(view.GONE);
            }
        });

    }

    private void loadCounterOfSid(String locationName) {

        Common.location=locationName;
        counterRef=FirebaseFirestore.getInstance().collection("Service_type")
                .document(locationName)
                .collection("Counter");
        counterRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Counter> list=new ArrayList<>();
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Counter counter=documentSnapshot.toObject(Counter.class);
                        counter.setCounterId(documentSnapshot.getId());
                        list.add(counter);
                    }
                    iCounterLoadListener.CounterLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iCounterLoadListener.CounterLoadFailed(e.getMessage());

            }
        });
    }

    @Override
    public void ServiceTypeLoadFaild(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void CounterLoadSuccess(List<Counter> counterNumberList) {
        MyServiceTypeAdapter adapter=new MyServiceTypeAdapter(getActivity(),counterNumberList);
        recyclerCounter.setAdapter(adapter);
        recyclerCounter.setVisibility(View.VISIBLE);

    }

    @Override
    public void CounterLoadFailed(String message) {
      Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
