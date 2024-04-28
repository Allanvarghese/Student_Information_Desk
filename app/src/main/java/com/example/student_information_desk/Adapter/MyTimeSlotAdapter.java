package com.example.student_information_desk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_information_desk.Common.Common;
import com.example.student_information_desk.Interface.IRecyclerItemSelectedListener;
import com.example.student_information_desk.Model.TimeSlot;
import com.example.student_information_desk.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {
    Context context;
    List<TimeSlot> timeSlotList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context){
        this.context=context;
        this.timeSlotList=new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }
    public MyTimeSlotAdapter(Context context,List<TimeSlot> timeSlotList){
        this.context=context;
        this.timeSlotList=timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(i)).toString());
        if(timeSlotList.size() == 0) //if all position is available just show list
        {
            myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white));
            myViewHolder.txt_time_slot_description.setText("Available");
            myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
                    .getColor(android.R.color.black));
            myViewHolder.txt_time_slot.setTextColor(context.getResources()
                    .getColor(android.R.color.black));

        }

        else //if have position is full booked
        {
            for(TimeSlot slotValue:timeSlotList)
            {
                //loop all time slot from server and set different color
                int slot=Integer.parseInt(slotValue.getSlot().toString());
                if(slot==i)//if slot == position
                {
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.darker_gray));
                    myViewHolder.txt_time_slot_description.setText("Full");
                    myViewHolder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    myViewHolder.txt_time_slot.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                }
            }

        }
        // Set the click listener for each time slot
        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                int currentPosition = myViewHolder.getAdapterPosition();
                Log.d("MyTimeSlotAdapter-MyViewHolder", "Position: " + currentPosition);
                if (currentPosition != RecyclerView.NO_POSITION && !timeSlotList.contains(new TimeSlot((long) currentPosition))) {
                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));

                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    intent.putExtra(Common.KEY_TIME_SLOT, currentPosition);
                    localBroadcastManager.sendBroadcast(intent);
                }
            }
        });


//        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
//            @Override
//            public void onItemSelectedListener(View view, int pos) {
//                Log.d("MyTimeSlotAdapter-MyViewHolder","pos :"+ pos);
//                if (!timeSlotList.contains(new TimeSlot((long) pos))) { // Check if the slot is not full
//                    // Update UI to indicate this slot is selected
//                    myViewHolder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
//
//                    // You might want to broadcast or handle the event that the slot is selected
//                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                    intent.putExtra(Common.KEY_TIME_SLOT, pos);
//                    localBroadcastManager.sendBroadcast(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView txt_time_slot,txt_time_slot_description;
//        CardView card_time_slot;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            card_time_slot=(CardView) itemView.findViewById(R.id.card_time_slot);
//            txt_time_slot=(TextView) itemView.findViewById(R.id.txt_time_slot);
//            txt_time_slot_description=(TextView) itemView.findViewById(R.id.txt_time_slot_description);
//        }
//    }
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txt_time_slot, txt_time_slot_description;
    CardView card_time_slot;

    IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

    public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener listener) {
        this.iRecyclerItemSelectedListener = listener;
    }

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
        txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
        txt_time_slot_description = (TextView) itemView.findViewById(R.id.txt_time_slot_description);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (iRecyclerItemSelectedListener != null) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}

}
