package com.example.student_information_desk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Interface.IRecyclerItemSelectedListener;
import com.example.student_information_desk.Model.Employee;
import com.example.student_information_desk.R;

import java.util.ArrayList;
import java.util.List;

public class MyEmployeeAdapter extends RecyclerView.Adapter<MyEmployeeAdapter.MyViewHolder>{
    Context context;
    List<Employee> employeeList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    public MyEmployeeAdapter(Context context,List<Employee> employeeList){
        this.context=context;
        this.employeeList=employeeList;
        cardViewList=new ArrayList<>();
        localBroadcastManager=LocalBroadcastManager.getInstance(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context)
                .inflate(R.layout.layout_employee,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_employee_name.setText(employeeList.get(i).getName());
        myViewHolder.ratingBar.setRating((float)employeeList.get(i).getRating());
        if(!cardViewList.contains(myViewHolder.card_employee))
            cardViewList.add(myViewHolder.card_employee);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background for all item not choice
                for(CardView cardView:cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }
                //set background for choice
                myViewHolder.card_employee.setCardBackgroundColor(
                        context.getResources()
                                .getColor(android.R.color.holo_orange_dark)
                );
                //send local broadcast to enable button next
                Intent intent=new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_EMPLOYEE_SELECTED,employeeList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return employeeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_employee_name;
        RatingBar ratingBar;
        CardView card_employee;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_employee=(CardView) itemView.findViewById(R.id.card_employee);
            txt_employee_name=(TextView) itemView.findViewById(R.id.txt_employee_name);
            ratingBar=(RatingBar) itemView.findViewById(R.id.rtb_employee);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
