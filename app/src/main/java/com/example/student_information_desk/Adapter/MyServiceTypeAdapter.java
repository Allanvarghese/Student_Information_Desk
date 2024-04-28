package com.example.student_information_desk.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.student_information_desk.Model.Counter;
import com.example.student_information_desk.R;

import java.util.ArrayList;
import java.util.List;

public class MyServiceTypeAdapter extends RecyclerView.Adapter<MyServiceTypeAdapter.MyViewHolder>{
    Context context;
    List<Counter> counterList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyServiceTypeAdapter(Context context,List<Counter> counterList){
        this.context=context;
        this.counterList=counterList;
        cardViewList=new ArrayList<>();
        localBroadcastManager=LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context)
                .inflate(R.layout.layout_counter,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.txt_counter_name.setText(counterList.get(i).getName());
        holder.txt_counter_address.setText(counterList.get(i).getAddress());

        if(!cardViewList.contains(holder.card_counter))
            cardViewList.add(holder.card_counter);
        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set white background for all cards that not be selected
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                //set orange color for only selected item
                holder.card_counter.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                //send broadcast to booking activity to enable button next
                Intent intent=new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_COUNTER_STORE,counterList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return counterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_counter_name,txt_counter_address;
        CardView card_counter;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener){
            this.iRecyclerItemSelectedListener=iRecyclerItemSelectedListener;
        }
        public MyViewHolder(View view){
            super(view);
            card_counter=(CardView)view.findViewById(R.id.card_counter);
            txt_counter_name=(TextView) view.findViewById(R.id.txt_counter_name);
            txt_counter_address=(TextView) view.findViewById(R.id.txt_counter_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
